package com.example.androidcourse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

enum class HabitType(val value: Int) {
    Study(0),
    Health(1),
    Beauty(2);

    companion object {
        private val values = values();
        fun getByValue(value: Int): HabitType {
            return values.firstOrNull { it.value == value }
                ?: throw Exception("Class HabitType doesn't contain value $value")
        }
    }
}

enum class Priority(val value: Int) {
    Low(0),
    Middle(1),
    High(2);

    companion object {
        private val values = values();
        fun getByValue(value: Int): Priority {
            return values.firstOrNull { it.value == value }
                ?: throw Exception("Class Priority doesn't contain value $value")
        }
    }
}

@Parcelize
class Habit(
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val habitType: HabitType = HabitType.Health,
    val repetitions: Int = 10,
    val periodicity: Int = 2,
    val color: String = "red",
    val id: UUID = UUID.randomUUID()
) : Parcelable