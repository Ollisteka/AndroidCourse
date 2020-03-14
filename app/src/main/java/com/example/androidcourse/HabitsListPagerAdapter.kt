package com.example.androidcourse

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HabitsListPagerAdapter(activity: AppCompatActivity, private val habits: List<Habit>) : FragmentStateAdapter(activity) {
    private var goodHabits: HabitListFragment? = null
    private var badHabits: HabitListFragment? = null

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                goodHabits =
                    HabitListFragment.newInstance(habits.filter { it.type == HabitType.Good })
                goodHabits!!
            }
            else -> {
                badHabits =
                    HabitListFragment.newInstance(habits.filter { it.type == HabitType.Bad })
                badHabits!!
            }
        }
    }

    override fun getItemCount() = 2

    fun notifyHabitChanged(habit: Habit) {
        if (habit.type == HabitType.Good) {
            goodHabits?.addOrUpdate(habit)
        } else {
            badHabits?.addOrUpdate(habit)
        }
    }
}