package com.example.androidcourse


import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
            val habit = habits[position];
            val sendIntent = Intent(view.context, EditHabitActivity::class.java)
            sendIntent.putExtra(EXTRA.NEW_HABIT, habit)
            sendIntent.putExtra(EXTRA.HABIT_POSITION, position)
            view.context.startActivity(sendIntent)
        }
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickListener {
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

        override fun onClick(view: View?, position: Int) {

        }

        override fun toString(): String {
            return super.toString() + "${nameView.text} (id: ${habitId})"
        }

        fun bind(habit: Habit) {
            habitId = habit.id
            nameView.text = habit.name
            descriptionView.text = habit.description
            colors.map { it.setBackgroundColor(Color.parseColor(habit.color)) }
            middlePriorityView.visibility = if (habit.priority < Priority.Middle) View.INVISIBLE else View.VISIBLE
            highPriorityView.visibility = if (habit.priority < Priority.High) View.INVISIBLE else View.VISIBLE

            val pluralDays = context.resources.getStringArray(R.array.days_plurals)
            val correctedDays = TextHelpers.getPluralWord(habit.periodicity, pluralDays)

            val pluralTimes = context.resources.getStringArray(R.array.times_plurals)
            val correctedTimes = TextHelpers.getPluralWord(habit.repetitions, pluralTimes)

            val every = context.resources.getString(R.string.every)
            val periodicity = "${habit.repetitions} $correctedTimes, $every ${habit.periodicity} $correctedDays"
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