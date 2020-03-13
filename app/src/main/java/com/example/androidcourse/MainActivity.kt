package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

const val LOG_TAG = "MainActivity"
const val HABITS = "HABITS"

class MainActivity : AppCompatActivity() {
    private lateinit var habitsListFragment: HabitListFragment;

    private var habits: MutableList<Habit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "Activity created")
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
        Log.d(LOG_TAG, "Instance state saved")
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(HABITS, habits.toTypedArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(LOG_TAG, "Instance state restored")
        super.onRestoreInstanceState(savedInstanceState)

        habits.clear()
        savedInstanceState.getParcelableArray(HABITS)?.map { habits.add(it as Habit) }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(LOG_TAG, "On new intent called")
        super.onNewIntent(intent)
        setIntent(intent)

        val newHabit: Habit = intent?.getParcelableExtra(EXTRA_NEW_HABIT) ?: return
        val position = intent.getIntExtra(EXTRA_HABIT_POSITION, -1)
        habitsListFragment.addOrUpdate(newHabit, position)
    }

    override fun onStart() {
        Log.d(LOG_TAG, "Activity started")
        super.onStart()
    }

    override fun onPause() {
        Log.d(LOG_TAG, "Activity paused")
        super.onPause()
    }

    override fun onResume() {
        Log.d(LOG_TAG, "Activity resumed")
        super.onResume()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "Activity stopped")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(LOG_TAG, "Activity restarted")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "Activity destroyed")
        super.onDestroy()
    }
}
