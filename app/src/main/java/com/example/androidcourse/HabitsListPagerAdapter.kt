package com.example.androidcourse

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HabitsListPagerAdapter(activity: AppCompatActivity, private var habits: MutableList<Habit>) : FragmentStateAdapter(activity) {
    private lateinit var goodHabits: HabitListFragment
    private lateinit var badHabits: HabitListFragment

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                goodHabits =
                    HabitListFragment.newInstance(habits.filter { it.type == HabitType.Good })
                goodHabits
            }
            else -> {
                badHabits =
                    HabitListFragment.newInstance(habits.filter { it.type == HabitType.Bad })
                badHabits
            }
        }
    }

    fun setHabits(habits: List<Habit>) {
        this.habits.clear()
        habits.map { this.habits.add(it) }
    }

    override fun getItemCount() = 2

    fun notifyHabitChanged(habit: Habit) {
        if (habit.type == HabitType.Good) {
            goodHabits.addOrUpdate(habit)
        } else {
            badHabits.addOrUpdate(habit)
        }
    }
}