package com.example.androidcourse


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_habit.view.*

/**
 * [RecyclerView.Adapter] that can display a [Habit]
 * TODO: Replace the implementation with code for your data type.
 */
class MyHabitRecyclerViewAdapter(
    private val habits: List<Habit>
) : RecyclerView.Adapter<MyHabitRecyclerViewAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.name
        val descriptionView: TextView = itemView.description

        override fun toString(): String {
            return super.toString() + " '" + descriptionView.text + "'"
        }

        fun bind(habit: Habit) {
            nameView.text = habit.name
            descriptionView.text = habit.description
        }
    }
}
