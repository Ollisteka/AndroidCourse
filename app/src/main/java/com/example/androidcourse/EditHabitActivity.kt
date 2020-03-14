package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_edit_habit.*

class EditHabitActivity : AppCompatActivity() {
    private lateinit var editHabitFragment: EditHabitFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)
        editHabitFragment =
            supportFragmentManager.findFragmentById(R.id.editHabitView) as EditHabitFragment;
        saveHabitButton.apply {
            setOnClickListener {
                val sendIntent = Intent(applicationContext, MainActivity::class.java)
                sendIntent.putExtra(EXTRA.NEW_HABIT, editHabitFragment.getHabit())
                startActivity(sendIntent)
            }
        }

        fillForEdit()
    }

    private fun fillForEdit() {
        if (!intent.hasExtra(EXTRA.NEW_HABIT)) {
            actionBar?.title = getString(R.string.newHabitActivity_barTitle)
            supportActionBar?.title = getString(R.string.newHabitActivity_barTitle)
            return
        }

        actionBar?.title = getString(R.string.editHabitActivity_barTitle)
        supportActionBar?.title = getString(R.string.editHabitActivity_barTitle)

        val habitToEdit = intent.getParcelableExtra<Habit>(EXTRA.NEW_HABIT) ?: return
        editHabitFragment.update(habitToEdit)
    }
}
