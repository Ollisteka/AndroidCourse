package com.example.androidcourse

import java.util.*

class Habit(val name: String, val description: String, val priority: Int, val periodicity: Long, val color: String) {
    val id: UUID = UUID.randomUUID()
}