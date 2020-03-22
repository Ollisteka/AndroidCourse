package com.example.androidcourse

import androidx.lifecycle.ViewModel
import java.util.*

enum class Sorts {
    NAME_ASC,
    NAME_DESC,
    CREATION_ASC,
    CREATION_DESC
}

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

        if (ascSort.size == 0 && descSort.size == 0)
            return filtered

        val comparator = if (ascSort.size > 0) {
            var comparator = compareBy(*(ascSort.toTypedArray()))
            descSort.forEach {
                comparator = comparator.thenByDescending(it)
            }
            comparator

        } else {
            var comparator = compareByDescending(descSort[0])
            for (i in 1 until descSort.size) {
                comparator = comparator.thenByDescending(descSort[i])
            }
            comparator
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

    var ascSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()
    var descSort: MutableList<Habit.() -> Comparable<*>> = mutableListOf()

    fun <T : Comparable<T>> sortBy(fn: Habit.() -> T) {
        clearSortBy(fn)
        ascSort.add(fn)
    }

    fun <T : Comparable<T>> sortByDesc(fn: Habit.() -> T) {
        clearSortBy(fn)
        descSort.add(fn)
    }

    fun <T : Comparable<T>> clearSortBy(fn: Habit.() -> T) {
        descSort.remove(fn)
        ascSort.remove(fn)
    }

    private fun getIndexedHabit(id: UUID) = habits.withIndex().find { it.value.id == id }
}