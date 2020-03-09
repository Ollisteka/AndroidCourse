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
import java.util.*


const val EXTRA_NEW_HABIT = "com.example.androidcourse.NEW_HABIT"

class EditHabitActivity : AppCompatActivity() {

    private lateinit var habitNameEdit: EditText
    private lateinit var habitDescriptionEdit: EditText
    private lateinit var habitRepetitions: EditText
    private lateinit var habitPeriodicity: EditText
    private lateinit var habitPrioritySpinner: Spinner
    private lateinit var habitTypeRadio: RadioGroup
    private var habitId: UUID? = null
    private var habitPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)

        habitNameEdit = findViewById(R.id.habitName)
        habitDescriptionEdit = findViewById(R.id.habitDescription)

        val saveButton = findViewById<Button>(R.id.saveHabitButton).apply {
            setOnClickListener {
                val sendIntent = Intent(applicationContext, MainActivity::class.java)
                sendIntent.putExtra(EXTRA_NEW_HABIT, getHabit())
                habitPosition?.let { sendIntent.putExtra(EXTRA_HABIT_POSITION, it) }
                startActivity(sendIntent)
            }
        }

        habitNameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                saveButton.isEnabled = !TextUtils.isEmpty(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        habitNameEdit.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
                habitNameEdit.error =
                    if (TextUtils.isEmpty((view as EditText).text)) getString(R.string.required_field_error) else null
        }

        habitPrioritySpinner = findViewById(R.id.habitPriority)
        habitTypeRadio = findViewById(R.id.habitTypeRadio)
        habitRepetitions = findViewById(R.id.habitRepetitions)
        habitPeriodicity = findViewById(R.id.habitPeriodicity)

        fillForEdit()
    }

    private fun fillForEdit() {
        if (!intent.hasExtra(EXTRA_NEW_HABIT)) {
            actionBar?.title = getString(R.string.newHabitActivity_barTitle)
            supportActionBar?.title = getString(R.string.newHabitActivity_barTitle)
            return
        }

        actionBar?.title = getString(R.string.editHabitActivity_barTitle)
        supportActionBar?.title = getString(R.string.editHabitActivity_barTitle)

        val habitToEdit = intent.getParcelableExtra<Habit>(EXTRA_NEW_HABIT) ?: return
        habitId = habitToEdit.id
        habitNameEdit.setText(habitToEdit.name)
        habitDescriptionEdit.setText(habitToEdit.description)
        habitPrioritySpinner.setSelection(habitToEdit.priority.value)
        habitTypeRadio.check(
            when (habitToEdit.habitType) {
                HabitType.Beauty -> R.id.radio_beauty
                HabitType.Health -> R.id.radio_health
                HabitType.Study -> R.id.radio_study
            }
        )
        habitRepetitions.setText(habitToEdit.repetitions.toString())
        habitPeriodicity.setText(habitToEdit.periodicity.toString())
        habitPosition = intent.getIntExtra(EXTRA_HABIT_POSITION, -1)
    }

    private fun getHabit(): Habit {
        val name = habitNameEdit.text.toString()
        val description = getValueOrDefault(habitDescriptionEdit, R.string.habitDescription)
        val priority = Priority.getByValue(habitPrioritySpinner.selectedItemPosition);
        val type =
            when (habitTypeRadio.checkedRadioButtonId) {
                R.id.radio_beauty -> HabitType.Beauty
                R.id.radio_health -> HabitType.Health
                R.id.radio_study -> HabitType.Study
                else -> throw Exception("You forgot to create new HabitType or handle it here")
            }

        val repetitions = getValueOrDefault(habitRepetitions, R.string.numberHint).toInt()
        val periodicity = getValueOrDefault(habitPeriodicity, R.string.numberHint).toInt()

        val id = habitId ?: UUID.randomUUID()
        return Habit(name, description, priority, type, repetitions, periodicity, id = id)
    }

    private fun getValueOrDefault(view: EditText, defaultResourceId: Int): String {
        return view.text.toString().let { if (TextUtils.isEmpty(it)) getString(defaultResourceId) else it }
    }
}
