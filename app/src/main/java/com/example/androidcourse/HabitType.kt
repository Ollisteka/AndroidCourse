package com.example.androidcourse

import android.content.Context

enum class HabitType(val value: Int) {
    Study(0),
    Health(1),
    Beauty(2);

    fun toLocalString(context: Context): String {
        return context.getString(getStringId())
    }

    private fun getStringId(): Int {
        return when (this) {
            Beauty -> R.string.beauty
            Health -> R.string.health
            Study -> R.string.study
        }
    }
}