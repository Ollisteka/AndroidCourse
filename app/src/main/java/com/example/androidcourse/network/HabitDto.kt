package com.example.androidcourse.network

import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import com.google.gson.annotations.SerializedName
import java.util.*

class HabitDto(
    @SerializedName("title")
    var name: String = "",
    var description: String = "",
    var priority: Priority = Priority.Low,
    var type: HabitType = HabitType.Good,
    @SerializedName("count")
    var repetitions: Int? = null,
    @SerializedName("frequency")
    var periodicity: Int? = null,
    var color: Int = -13070788,
    @SerializedName("uid")
    var id: UUID? = null,
    @SerializedName("date")
    var editDate: Calendar = Calendar.getInstance()
)