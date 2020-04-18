package com.example.androidcourse.fragments

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourse.*
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.viewmodels.HabitsViewModel
import kotlinx.android.synthetic.main.fragment_habit_list.*
import kotlin.math.abs
import kotlin.math.min


private const val ARGS_HABIT_TYPE = "HABIT_TYPE"

class HabitListFragment : Fragment(), IHabitsObserver {
    private var habits: MutableList<Habit> = mutableListOf()
    private val model: HabitsViewModel by activityViewModels()

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

        habits = model.habitsByType[habitType]?.value?.toMutableList() ?: mutableListOf()
        model.initObserve(habitType).observe(viewLifecycleOwner, Observer { })
        model.habitsByType[habitType]?.observe(viewLifecycleOwner, Observer { updateHabits(it) })
        model.onHabitDeleted(habitType)?.observe(viewLifecycleOwner, Observer { onHabitDeleted(it.deleted, it.position) })
        viewManager = LinearLayoutManager(context)
        viewAdapter = MyHabitRecyclerViewAdapter(habits)

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(habitsRecyclerView)

        habitsRecyclerView.apply {
            layoutManager = viewManager

            adapter = viewAdapter
        }
        togglePlaceHolder()
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                model.deleteHabit(habits[position], position)
            }
        }

    private fun onHabitDeleted(deleted: Boolean, position: Int) {
        if (deleted) {
            showToast(context, R.string.delete_success, Gravity.CENTER)
            viewAdapter.notifyItemRemoved(position)
            return
        }
        viewAdapter.notifyItemChanged(position)
        showToast(context, R.string.error_delete, Gravity.CENTER)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getSerializable(ARGS_HABIT_TYPE)?.let {
            habitType = it as HabitType
        }

        (activity as IHabitsObservable).addHabitsObserver(this)
    }

    private fun updateHabits(newHabits: List<Habit>) {
        val oldSize = habits.size

        updateHabits(oldSize, newHabits)
        if (oldSize < newHabits.size) {
            insertHabits(newHabits, oldSize)
        } else if (oldSize > newHabits.size) {
            removeTail(newHabits.size, oldSize - newHabits.size)
        }

        togglePlaceHolder()
    }

    private fun updateHabits(oldSize: Int, newHabits: List<Habit>) {
        for (i in 0 until min(oldSize, newHabits.size)) {
            if (habits[i] != newHabits[i]) {
                habits[i] = newHabits[i]
                viewAdapter.notifyItemChanged(i)
            }
        }
    }

    private fun insertHabits(newHabits: List<Habit>, oldSize: Int) {
        for (i in oldSize until newHabits.size) {
            habits.add(newHabits[i])
        }

        viewAdapter.notifyItemRangeInserted(oldSize, abs(oldSize - newHabits.size))
    }

    private fun removeTail(positionStart: Int, itemsCount: Int) {
        for (i in 0 until itemsCount) {
            habits.removeAt(habits.size - 1)
        }

        viewAdapter.notifyItemRangeRemoved(positionStart, itemsCount)
    }

    private fun togglePlaceHolder() {
        list_empty.visibility = if (habits.isEmpty()) View.VISIBLE else View.GONE
    }
}