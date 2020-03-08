package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_NEW_HABIT = "com.example.androidcourse.NEW_HABIT"

class EditHabitActivity : AppCompatActivity() {

    private lateinit var habitName: EditText
    private lateinit var habitDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)

        habitName = findViewById(R.id.habitName)
        habitDescription = findViewById(R.id.habitDescription)

        val saveButton = findViewById<Button>(R.id.saveHabitButton).apply {
            setOnClickListener {
                val sendIntent = Intent(applicationContext, MainActivity::class.java)
                sendIntent.putExtra(EXTRA_NEW_HABIT, getHabit())
                startActivity(sendIntent)
            }
        }
    }

    private fun getHabit(): Habit {
        var name = habitName.text.toString()
        if (name == "")
            name = habitName.hint.toString()
        val description = habitDescription.text.toString()

        return Habit(name, description)
    }
}
