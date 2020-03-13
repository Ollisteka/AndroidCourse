package com.example.androidcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_habit_list.*

private const val SAVED_HABITS = "SAVED_HABITS"

class HabitListFragment : Fragment() {
    private var habits: MutableList<Habit> = mutableListOf()
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habits.clear()
        savedInstanceState?.getParcelableArray(SAVED_HABITS)?.map { habits.add(it as Habit) }

        viewManager = LinearLayoutManager(context)
        viewAdapter =
            MyHabitRecyclerViewAdapter(habits)

        habitsRecyclerView.apply {
            layoutManager = viewManager

            adapter = viewAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(SAVED_HABITS, habits.toTypedArray())
    }

    fun setHabits(habits: List<Habit>) {
        this.habits.clear()
        habits.map { this.habits.add(it) }
        viewAdapter.notifyDataSetChanged()
    }

    fun addOrUpdate(newHabit: Habit, position: Int) {
        if (habits.any { it.id == newHabit.id } && position >= 0 && position < habits.size)
            updateHabit(newHabit, position)
        else addNewHabit(newHabit)
    }

    private fun updateHabit(habit: Habit, position: Int) {
        habits[position] = habit
        viewAdapter.notifyItemChanged(position)
    }

    private fun addNewHabit(habit: Habit) {
        habits.add(habit)
        viewAdapter.notifyItemInserted(habits.size - 1)
    }
}