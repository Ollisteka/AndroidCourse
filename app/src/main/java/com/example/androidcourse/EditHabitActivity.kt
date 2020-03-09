package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


const val EXTRA_NEW_HABIT = "com.example.androidcourse.NEW_HABIT"

class EditHabitActivity : AppCompatActivity() {

    private lateinit var habitName: EditText
    private lateinit var habitDescription: EditText
    private lateinit var habitPrioritySpinner: Spinner
    private lateinit var habitTypeRadio: RadioGroup

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

        habitName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                saveButton.isEnabled = !TextUtils.isEmpty(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        habitName.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
                habitName.error = if (TextUtils.isEmpty((view as EditText).text)) "Поле обязательно" else null
        }

        habitPrioritySpinner = findViewById(R.id.habitPriority)
        habitTypeRadio = findViewById(R.id.habitTypeRadio)
    }

    private fun getHabit(): Habit {
        val name = habitName.text.toString()
        val description = habitDescription.text.toString()
        val priority = Priority.getByValue(habitPrioritySpinner.selectedItemPosition);
        val type = HabitType.getByValue(habitTypeRadio.checkedRadioButtonId);

        return Habit(name, description, priority, type)
    }
}
