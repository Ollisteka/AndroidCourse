package com.example.androidcourse

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val EXTRA_COUNTER = "com.example.androidcourse.COUNTER"
const val LOG_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val habits: MutableList<Habit> = mutableListOf(
        Habit("Привычка #1", "Описание", 1, 10, "white")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "Activity created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyHabitRecyclerViewAdapter(habits)

        recyclerView = findViewById<RecyclerView>(R.id.habitsRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        val addHabitButton: View = findViewById(R.id.addHabitButton)
        addHabitButton.setOnClickListener { view ->
            habits.add(Habit("Привычка #${habits.size + 1}", "Описание", 1, 10, "white"))
            viewAdapter.notifyItemInserted(habits.size - 1)
        }
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
