package com.example.androidcourse

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "habits")
class Habit(
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val type: HabitType = HabitType.Good,
    val repetitions: Int = 10,
    val periodicity: Int = 2,
    val color: String = "#388E3C",
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val creationDate: Calendar = Calendar.getInstance()
) : Parcelable {
}