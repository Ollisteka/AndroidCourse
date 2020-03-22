package com.example.androidcourse

import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HabitsViewModel : ViewModel() {
    private val habits: MutableList<Habit> by lazy {
        loadHabits()
    }

    private fun loadHabits(): MutableList<Habit> {
        return mutableListOf(
            Habit("Старая", "Описание", periodicity = 1, type = HabitType.Good),
            Habit("AХорошая", "Описание", periodicity = 10, type = HabitType.Good),
            Habit("БХорошая", "Описание", periodicity = 19, type = HabitType.Good),
            Habit("ВХорошая", "Описание", periodicity = 6, type = HabitType.Good),
            Habit("ГХорошая", "Описание", periodicity = 6, type = HabitType.Good),
            Habit("Хорошая", "Описание", periodicity = 12, type = HabitType.Good),
            Habit("Новая", "Описание", periodicity = 100, type = HabitType.Good)
        )
    }

    fun getHabits(habitType: HabitType): List<Habit> {
        val filtered = habits.filter { it.type == habitType }

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
            habits[existingHabit.index] = newHabit
        } else {
            habits.add(newHabit)
        }
    }

    fun findById(id: UUID): Habit? {
        return getIndexedHabit(id)?.value
    }

    private var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    private var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    val dataSetChanged: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun <T : Comparable<T>> sortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.add(fn)
        dataSetChanged.value = !dataSetChanged.value!!
    }

    private fun <T : Comparable<T>> sortByDesc(fn: Habit.() -> T) {
        ascSort.remove(fn)
        descSort.add(fn)
        dataSetChanged.value = !dataSetChanged.value!!
    }

    private fun <T : Comparable<T>> clearSortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.remove(fn)
        dataSetChanged.value = !dataSetChanged.value!!
    }

    private fun getIndexedHabit(id: UUID) = habits.withIndex().find { it.value.id == id }

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
