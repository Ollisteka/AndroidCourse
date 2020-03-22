package com.example.androidcourse

import java.util.*

interface IHabitsObserver {
    val habitType: HabitType

    fun onHabitEdit(id: UUID)
    fun onHabitDelete(id: UUID)
    fun notifyDataSetHasChanged()
}
