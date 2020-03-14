package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

const val HABITS = "HABITS"

class MainActivity : AppCompatActivity() {
    private lateinit var habitsListFragment: HabitListFragment;

    private var habits: MutableList<Habit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        habitsListFragment =
            supportFragmentManager.findFragmentById(R.id.habitsList) as HabitListFragment;
        habitsListFragment.setHabits(habits)
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

        val newHabit: Habit = intent?.getParcelableExtra(EXTRA_NEW_HABIT) ?: return
        val position = intent.getIntExtra(EXTRA_HABIT_POSITION, -1)
        habitsListFragment.addOrUpdate(newHabit, position)
    }
}
