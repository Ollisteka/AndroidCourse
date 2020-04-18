package com.example.androidcourse.viewmodels

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.LOG_TAGS
import com.example.androidcourse.database.HabitsDatabase
import com.example.androidcourse.network.UUIDDto
import com.example.androidcourse.network.isOnline
import com.example.androidcourse.network.service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitDeletedEvent(val deleted: Boolean, val position: Int)

class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    private var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    private var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    private var habitsToFilter: MutableList<Habit> = mutableListOf()

    val habitsByType = mutableMapOf<HabitType, MutableLiveData<List<Habit>>>(
        HabitType.Bad to MutableLiveData(listOf()),
        HabitType.Good to MutableLiveData(listOf())
    )

    private val habitDeleted = mutableMapOf<HabitType, MutableLiveData<HabitDeletedEvent>>(
        HabitType.Bad to MediatorLiveData(),
        HabitType.Good to MediatorLiveData()
    )

    fun onHabitDeleted(type: HabitType): LiveData<HabitDeletedEvent>? {
        return habitDeleted[type]
    }

    private var _searchWord = MutableLiveData("")
    var searchWord: String
        get() = _searchWord.value ?: ""
        set(value) {
            if (value != _searchWord.value) {
                _searchWord.value = value
            }
        }

    fun importHabits(): Boolean {
        return if (isOnline(app.applicationContext)) {
            viewModelScope.launch(Dispatchers.IO) {
                service.getHabits()?.map { habitsDao?.upsert(it) }
            }
            true
        } else {
            false
        }
    }

    fun initObserve(habitType: HabitType): MediatorLiveData<List<Habit>> {
        return MediatorLiveData<List<Habit>>().apply {
            addSource(habitsDao?.habits!!) {
                habitsToFilter = it.toMutableList()
                habitsByType[habitType]?.value = getHabits(it, habitType)
            }
            addSource(_searchWord) {
                habitsByType[habitType]?.value = getHabits(habitsToFilter, habitType)
            }
        }
    }

    private fun matches(habit: Habit): Boolean = TextUtils.isEmpty(searchWord) || habit.name.contains(searchWord, true)

    private fun getHabits(newHabits: List<Habit>?, habitType: HabitType): List<Habit> {
        val filtered = newHabits?.filter { it.type == habitType && matches(it) }

        val comparator = if (ascSort.size > 0) {
            var comparator = compareBy(*(ascSort.toTypedArray()))
            descSort.forEach {
                comparator = comparator.thenByDescending(it)
            }
            comparator

        } else if (descSort.size > 0) {
            var comparator = compareByDescending(descSort[0])
            for (i in 1 until descSort.size) {
                comparator = comparator.thenByDescending(descSort[i])
            }
            comparator
        } else {
            compareBy(Habit::editDate)
        }
        return filtered?.sortedWith(comparator) ?: listOf()
    }

    fun <T : Comparable<T>> sortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.add(fn)
        _searchWord.value = _searchWord.value
    }

    fun <T : Comparable<T>> sortByDesc(fn: Habit.() -> T) {
        ascSort.remove(fn)
        descSort.add(fn)
        _searchWord.value = _searchWord.value
    }

    fun <T : Comparable<T>> clearSortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.remove(fn)
        _searchWord.value = _searchWord.value
    }

    fun deleteHabit(habit: Habit, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            service.deleteHabit(UUIDDto(habit.id))
        }
        viewModelScope.launch(Dispatchers.IO) {
            val deleted = deleteFromDb(habit)
            habitDeleted[habit.type]?.postValue(HabitDeletedEvent(deleted, position))
        }
    }

    private fun deleteFromDb(id: Habit): Boolean {
        try {
            habitsDao?.deleteHabit(id)
        } catch (e: Exception) {
            Log.e(LOG_TAGS.DATABASE, "При удалении привычки  возникла ошибка", e)
            return false
        }
        return true
    }
}
