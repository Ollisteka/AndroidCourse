package com.example.androidcourse


import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourse.core.EXTRA
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.Priority
import kotlinx.android.synthetic.main.fragment_habit.view.*
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Habit]
 */
class MyHabitRecyclerViewAdapter(
    private val habits: List<Habit>
) : RecyclerView.Adapter<MyHabitRecyclerViewAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_habit, parent, false)
        return HabitViewHolder(view).listen { position, _ ->
            val habit = habits[position]
            val sendIntent = Intent(view.context, EditHabitActivity::class.java)
            sendIntent.putExtra(EXTRA.HABIT_ID, habit.id)
            view.context.startActivity(sendIntent)
        }
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickListener, View.OnLongClickListener {
        private val nameView: TextView = itemView.name
        private val descriptionView: TextView = itemView.description
        private val lowPriorityView: View = itemView.priorityLow
        private val middlePriorityView: View = itemView.priorityMiddle
        private val highPriorityView: View = itemView.priorityHigh
        private val periodicityView: TextView = itemView.periodicity
        private val typeView: TextView = itemView.type
        private val context = itemView.context
        private var habitId: UUID? = null

        private val colors = listOf(lowPriorityView, middlePriorityView, highPriorityView)

        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(view: View?, position: Int) {

        }

        override fun onLongClick(v: View?): Boolean {
            showToast(context, R.string.swipe_to_delete, Gravity.CENTER)
            return true
        }

        override fun toString(): String {
            return super.toString() + "${nameView.text} (id: ${habitId})"
        }

        fun bind(habit: Habit) {
            habitId = habit.id
            nameView.text = habit.name
            descriptionView.text = habit.description
            colors.map { it.setBackgroundColor(habit.color) }
            middlePriorityView.visibility = if (habit.priority < Priority.Middle) View.INVISIBLE else View.VISIBLE
            highPriorityView.visibility = if (habit.priority < Priority.High) View.INVISIBLE else View.VISIBLE

            val pluralTimes = context.resources.getQuantityString(R.plurals.times, habit.repetitions)
            val pluralDays = context.resources.getQuantityString(R.plurals.days, habit.periodicity)
            val every = context.resources.getString(R.string.every)
            val periodicity = "${habit.repetitions} $pluralTimes, $every ${habit.periodicity} $pluralDays"
            periodicityView.text = periodicity
            typeView.text = habit.type.toLocalString(context)
        }
    }
}

interface OnItemClickListener {
    fun onClick(view: View?, position: Int)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}