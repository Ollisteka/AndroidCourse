package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androidcourse.core.EXTRA
import com.example.androidcourse.core.Habit
import com.example.androidcourse.fragments.EditHabitFragment
import kotlinx.android.synthetic.main.activity_main.*
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
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        if (!intent.hasExtra(EXTRA.NEW_HABIT)) {
            supportActionBar?.title = getString(R.string.newHabitActivity_barTitle)
            return
        }

        supportActionBar?.title = getString(R.string.editHabitActivity_barTitle)

        val habitToEdit = intent.getParcelableExtra<Habit>(EXTRA.NEW_HABIT) ?: return
        editHabitFragment.update(habitToEdit)
    }
}
