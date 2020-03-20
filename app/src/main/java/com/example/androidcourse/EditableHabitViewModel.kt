package com.example.androidcourse

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import java.util.*

class EditableHabitViewModel : ViewModel() {
    var name: String = ""
    var description: String = ""
    var priority: Priority = Priority.Low
    var type: HabitType = HabitType.Good
    var repetitions: Int? = null
    var periodicity: Int? = null
    var color: String = "#388E3C"
    private var id: UUID = UUID.randomUUID()

    var stringRepetitions: String
        get() = repetitions?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != repetitions)
                repetitions = int
        }

    var stringPeriodicity: String
        get() = periodicity?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != periodicity)
                periodicity = int
        }

    fun update(habit: Habit) {
        name = habit.name
        description = habit.description
        priority = habit.priority
        type = habit.type
        repetitions = habit.repetitions
        periodicity = habit.periodicity
        color = habit.color
        id = habit.id
    }

    fun getHabit(): Habit {
        return Habit(name, description, priority, type, repetitions ?: 10, periodicity ?: 10, color, id)
    }

}