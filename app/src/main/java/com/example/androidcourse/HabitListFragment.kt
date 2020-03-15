package com.example.androidcourse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_habit_list.*
import java.util.*

private const val SAVED_HABITS = "SAVED_HABITS"
private const val ARGS_HABIT_TYPE = "HABIT_TYPE"

class HabitListFragment : Fragment(), IHabitsWatcher {
    private var habits: MutableList<Habit> = mutableListOf()
    private var habitsProvider: IHabitsProvider? = null

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override lateinit var habitType: HabitType

    companion object {
        fun newInstance(type: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARGS_HABIT_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
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

        arguments?.getSerializable(ARGS_HABIT_TYPE)?.let {
            habitType = it as HabitType
        }

        if (savedInstanceState != null) {
            savedInstanceState.getParcelableArray(SAVED_HABITS)?.let { habitsToLoad ->
                habits = habitsToLoad.map { it as Habit }.toMutableList()
            }
        } else {
            habits = habitsProvider?.getHabits(habitType)?.toMutableList() ?: mutableListOf()
        }

        viewManager = LinearLayoutManager(context)
        viewAdapter = MyHabitRecyclerViewAdapter(habits)

        habitsRecyclerView.apply {
            layoutManager = viewManager

            adapter = viewAdapter
        }

        habitsProvider?.addHabitsWatcher(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(SAVED_HABITS, habits.toTypedArray())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        habitsProvider = activity as IHabitsProvider
    }

    override fun onHabitDelete(id: UUID) {
        habits.withIndex().find { it.value.id == id }?.let { (index, _) ->
            habits.removeAt(index)
            viewAdapter.notifyItemRemoved(index)
        }
    }

    override fun onHabitEdit(habit: Habit) {
        val existingHabit = habits.withIndex().find { it.value.id == habit.id }
        if (existingHabit != null) {
            updateHabit(habit, existingHabit.index)
        } else addNewHabit(habit)
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