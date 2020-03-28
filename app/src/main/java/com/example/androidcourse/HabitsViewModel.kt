package com.example.androidcourse

import android.app.Application
import android.text.TextUtils
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    private var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    private var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    private var habitsList: MutableList<Habit> = habitsDao?.habits?.value?.toMutableList() ?: mutableListOf()
    val habitsByType = mutableMapOf<HabitType, MutableLiveData<List<Habit>>>(
        HabitType.Bad to MutableLiveData(getHabits(HabitType.Bad)),
        HabitType.Good to MutableLiveData(getHabits(HabitType.Good))
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
                habitsByType[habitType]?.value = getHabits(habitType)
            }
        }
    }

    private fun updateSource(newHabits: List<Habit>?, habitType: HabitType) {
        habitsList = newHabits?.toMutableList() ?: mutableListOf()
        habitsByType[habitType]?.value = getHabits(habitType)
    }

    private fun matches(habit: Habit): Boolean = TextUtils.isEmpty(searchWord) || habit.name.contains(searchWord, true)

    private fun getHabits(habitType: HabitType): List<Habit> {
        val filtered = habitsList.filter { it.type == habitType && matches(it) }

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
        return filtered.sortedWith(comparator)
    }

    fun addOrUpdate(newHabit: Habit) = habitsDao?.upsert(newHabit)

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
