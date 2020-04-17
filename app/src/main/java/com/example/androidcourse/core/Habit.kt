package com.example.androidcourse.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androidcourse.database.CalendarConverter
import com.example.androidcourse.database.HabitTypeConverter
import com.example.androidcourse.database.PriorityConverter
import com.example.androidcourse.database.UUIDConverter
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "habits")
@TypeConverters(PriorityConverter::class, HabitTypeConverter::class, CalendarConverter::class, UUIDConverter::class)
class Habit(
    @SerializedName("title")
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val type: HabitType = HabitType.Good,
    @SerializedName("count")
    val repetitions: Int = 10,
    @SerializedName("frequency")
    val periodicity: Int = 2,
    val color: String = "#388E3C",
    @PrimaryKey
    @SerializedName("uid")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("date")
    val creationDate: Calendar = Calendar.getInstance()
)