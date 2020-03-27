package com.example.androidcourse

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitsDao {
    @get:Query("SELECT * FROM habits")
    val habits: List<Habit>?

    @Insert
    fun insert(habit: Habit)

    @Update
    fun update(habit: Habit)
}