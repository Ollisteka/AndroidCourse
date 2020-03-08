package com.example.androidcourse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

enum class HabitType {
    Health,
    Beauty,
    Study
}

@Parcelize
class Habit(
    val name: String,
    val description: String,
    val priority: Int = 1,
    val habitType: HabitType = HabitType.Health,
    val periodicity: Long = 100,
    val color: String = "red",
    val id: UUID = UUID.randomUUID()
) : Parcelable