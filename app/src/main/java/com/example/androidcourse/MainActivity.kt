package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val LOG_TAG = "MainActivity"
const val HABITS = "HABITS"

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var habits: MutableList<Habit> = mutableListOf(
        Habit("Привычка #1", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #2", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #3", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #4", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #4", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #4", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #1231", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #1", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #4", "Описание", Priority.Low, HabitType.Health, 10, "white"),
        Habit("Привычка #4", "Описание", Priority.Low, HabitType.Health, 10, "white")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "Activity created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyHabitRecyclerViewAdapter(habits)

        recyclerView = findViewById<RecyclerView>(R.id.habitsRecyclerView).apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        val addHabitButton: View = findViewById(R.id.addHabitButton)
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
        //now getIntent() should always return the last received intent

        val newHabit: Habit = intent?.getParcelableExtra(EXTRA_NEW_HABIT) ?: return
        val position = intent.getIntExtra(EXTRA_HABIT_POSITION, -1)
        if (habits.any { it.id == newHabit.id } && position >= 0 && position < habits.size)
            updateHabit(newHabit, position)
        else addNewHabit(newHabit)
    }

    private fun addNewHabit(habit: Habit) {
        habits.add(habit)
        viewAdapter.notifyItemInserted(habits.size - 1)
    }

    private fun updateHabit(habit: Habit, position: Int) {
        habits[position] = habit
        viewAdapter.notifyItemChanged(position)
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
