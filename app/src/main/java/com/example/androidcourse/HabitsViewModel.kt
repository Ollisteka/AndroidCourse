package com.example.androidcourse

import android.app.Application
import android.text.TextUtils
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*
class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val habits: MutableList<Habit> by lazy {
        loadHabits().toMutableList()
    }

    val habitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData(loadHabits())
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    private fun loadHabits(): List<Habit> {
        return habitsDao?.habits ?: listOf()
    }

    private var _searchWord = ""
    var searchWord
        get() = _searchWord
        set(value) {
            if (value != _searchWord) {
                _searchWord = value
                habitsLiveData.value = habits
            }
        }

    private fun matches(habit: Habit): Boolean = TextUtils.isEmpty(searchWord) || habit.name.contains(searchWord, true)

    fun getHabits(habitType: HabitType): List<Habit> {
        val filtered = habits.filter { it.type == habitType && matches(it) }

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

    fun addOrUpdate(newHabit: Habit) {
        val existingHabit = getIndexedHabit(newHabit.id)
        if (existingHabit != null) {
            habitsDao?.update(newHabit)
            habits[existingHabit.index] = newHabit
        } else {
            habitsDao?.insert(newHabit)
            habits.add(newHabit)
        }
        habitsLiveData.value = habits
    }

    private var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    private var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    private fun <T : Comparable<T>> sortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.add(fn)
        habitsLiveData.value = habits
    }

    private fun <T : Comparable<T>> sortByDesc(fn: Habit.() -> T) {
        ascSort.remove(fn)
        descSort.add(fn)
        habitsLiveData.value = habits
    }

    private fun <T : Comparable<T>> clearSortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.remove(fn)
        habitsLiveData.value = habits
    }

    private fun getIndexedHabit(id: UUID): IndexedValue<Habit>? = habits.withIndex().find { it.value.id == id }

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
