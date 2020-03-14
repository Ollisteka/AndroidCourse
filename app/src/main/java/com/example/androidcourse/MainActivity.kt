package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

private const val HABITS = "HABITS"

class MainActivity : AppCompatActivity() {
    private lateinit var habitsPagerAdapter: HabitsListPagerAdapter

    private var habits: MutableList<Habit> = mutableListOf(
        Habit("Хорошая", "Описание", type = HabitType.Good),
        Habit("Плохая", "Описание", type = HabitType.Bad)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        habitsPagerAdapter = HabitsListPagerAdapter(this, habits)
        pager.adapter = habitsPagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = if (position == 0) "Хорошие" else "Плохие"
        }.attach()

        addHabitButton.setOnClickListener {
            val sendIntent = Intent(applicationContext, EditHabitActivity::class.java)
            startActivity(sendIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(HABITS, habits.toTypedArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        habits.clear()
        savedInstanceState.getParcelableArray(HABITS)?.map { habits.add(it as Habit) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        val newHabit: Habit = intent?.getParcelableExtra(EXTRA.NEW_HABIT) ?: return
        addOrUpdate(newHabit)
    }

    private fun addOrUpdate(newHabit: Habit) {
        val existingHabit = habits.withIndex().find { it.value.id == newHabit.id }
        if (existingHabit != null) {
            updateHabit(newHabit, existingHabit.index)
        } else addNewHabit(newHabit)
        habitsPagerAdapter.setHabits(habits)
        habitsPagerAdapter.notifyHabitChanged(newHabit)
    }

    private fun updateHabit(habit: Habit, position: Int) {
        habits[position] = habit
    }

    private fun addNewHabit(habit: Habit) {
        habits.add(habit)
    }
}
