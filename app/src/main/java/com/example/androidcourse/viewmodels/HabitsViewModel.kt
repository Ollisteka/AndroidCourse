package com.example.androidcourse.viewmodels

import android.app.Application
import android.text.TextUtils
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidcourse.R
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.database.HabitsDatabase


class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    private var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    private var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    val habitsByType = mutableMapOf<HabitType, MutableLiveData<List<Habit>>>(
        HabitType.Bad to MutableLiveData(getHabits(habitsDao?.habits?.value, HabitType.Bad)),
        HabitType.Good to MutableLiveData(getHabits(habitsDao?.habits?.value, HabitType.Good))
    )

    private var _searchWord = MutableLiveData("")
    var searchWord: String
        get() = _searchWord.value ?: ""
        set(value) {
            if (value != _searchWord.value) {
                _searchWord.value = value
            }
        }

    fun initObserve(habitType: HabitType): MediatorLiveData<List<Habit>> {
        return MediatorLiveData<List<Habit>>().apply {
            addSource(habitsDao?.habits!!) {
                updateSource(it, habitType)
            }
            addSource(_searchWord) {
                updateSource(habitsDao.habits?.value, habitType)
            }
        }
    }

    private fun updateSource(newHabits: List<Habit>?, habitType: HabitType) {
        habitsByType[habitType]?.value = getHabits(newHabits, habitType)
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
            compareBy(Habit::creationDate)
        }
        return filtered?.sortedWith(comparator) ?: listOf()
    }

    private fun <T : Comparable<T>> sortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.add(fn)
        _searchWord.value = _searchWord.value
    }

    private fun <T : Comparable<T>> sortByDesc(fn: Habit.() -> T) {
        ascSort.remove(fn)
        descSort.add(fn)
        _searchWord.value = _searchWord.value
    }

    private fun <T : Comparable<T>> clearSortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.remove(fn)
        _searchWord.value = _searchWord.value
    }

    val periodicitySortListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radio_periodicity_asc -> sortBy(Habit::periodicity)
            R.id.radio_periodicity_desc -> sortByDesc(Habit::periodicity)
            R.id.radio_periodicity_none -> clearSortBy(Habit::periodicity)
        }
    }

    val nameSortListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radio_name_asc -> sortBy(Habit::name)
            R.id.radio_name_desc -> sortByDesc(Habit::name)
            R.id.radio_name_none -> clearSortBy(Habit::name)
        }
    }
}
