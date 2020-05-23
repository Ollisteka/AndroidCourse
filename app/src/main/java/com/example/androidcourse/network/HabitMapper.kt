package com.example.androidcourse.network

import com.example.androidcourse.core.Habit
import java.util.*

class HabitMapper {
    companion object {
        fun toDto(habit: Habit): HabitDto {
            return HabitDto(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.repetitions,
                habit.periodicity,
                habit.color,
                habit.id,
                habit.editDate
            )
        }

        fun fromDto(habit: HabitDto): Habit {
            return Habit(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.repetitions ?: 10,
                habit.periodicity ?: 10,
                habit.color,
                habit.id ?: UUID.randomUUID(),
                habit.editDate
            )
        }
    }
}