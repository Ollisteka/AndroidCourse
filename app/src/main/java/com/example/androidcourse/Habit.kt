package com.example.androidcourse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Habit(
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val type: HabitType = HabitType.Health,
    val repetitions: Int = 10,
    val periodicity: Int = 2,
    val color: String = "#388E3C",
    val id: UUID = UUID.randomUUID()
) : Parcelable