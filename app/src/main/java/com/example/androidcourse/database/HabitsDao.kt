package com.example.androidcourse.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidcourse.core.Habit


@Dao
interface HabitsDao {
    @get:Query("SELECT * FROM habits")
    val habits: LiveData<List<Habit>>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(habit: Habit): Long

    @Update
    fun update(habit: Habit)

    @Transaction
    fun upsert(habit: Habit) {
        val id: Long = insert(habit)
        if (id == -1L) {
            update(habit)
        }
    }
}