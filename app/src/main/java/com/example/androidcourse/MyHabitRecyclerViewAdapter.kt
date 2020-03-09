package com.example.androidcourse


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_habit.view.*

const val EXTRA_HABIT_POSITION = "HabitPosition"

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
            sendIntent.putExtra(EXTRA_NEW_HABIT, habit)
            sendIntent.putExtra(EXTRA_HABIT_POSITION, position)
            view.context.startActivity(sendIntent)
        }
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickListener {
        val nameView: TextView = itemView.name
        val descriptionView: TextView = itemView.description

        override fun onClick(view: View?, position: Int) {

        }

        override fun toString(): String {
            return super.toString() + " '" + descriptionView.text + "'"
        }

        fun bind(habit: Habit) {
            nameView.text = habit.name
            descriptionView.text = habit.description
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