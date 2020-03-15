package com.example.androidcourse

import java.util.*

interface IHabitsWatcher {
    val habitType: HabitType

    fun onHabitEdit(habit: Habit)
    fun onHabitDelete(id: UUID)
}
