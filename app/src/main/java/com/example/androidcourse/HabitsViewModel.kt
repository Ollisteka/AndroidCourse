package com.example.androidcourse

import androidx.lifecycle.ViewModel
import java.util.*


class HabitsViewModel : ViewModel() {
    private val habits: MutableList<Habit> by lazy {
        loadHabits()
    }

    private fun loadHabits(): MutableList<Habit> {
        return mutableListOf(
            Habit("Хорошая", "Описание", type = HabitType.Good),
            Habit("Хорошая1", "Описание", type = HabitType.Good),
            Habit("Хорошая2", "Описание", type = HabitType.Good),
            Habit("Хорошая3", "Описание", type = HabitType.Good),
            Habit("Хорошая4", "Описание", type = HabitType.Good),
            Habit("Хорошая4", "Описание", type = HabitType.Good),
            Habit("Хорошая4", "Описание", type = HabitType.Good),
            Habit("Плохая", "Описание", type = HabitType.Bad)
        )
    }

    fun getHabits(habitType: HabitType): List<Habit> {
        return habits.filter { it.type == habitType }
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

    private fun getIndexedHabit(id: UUID) = habits.withIndex().find { it.value.id == id }
}