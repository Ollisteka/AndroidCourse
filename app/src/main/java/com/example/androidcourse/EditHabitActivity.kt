package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_habit.*
import java.util.*


const val EXTRA_NEW_HABIT = "com.example.androidcourse.NEW_HABIT"

class EditHabitActivity : AppCompatActivity() {
    private var habitId: UUID? = null
    private var habitPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)

        saveHabitButton.apply {
            setOnClickListener {
                val sendIntent = Intent(applicationContext, MainActivity::class.java)
                sendIntent.putExtra(EXTRA_NEW_HABIT, getHabit())
                habitPosition?.let { sendIntent.putExtra(EXTRA_HABIT_POSITION, it) }
                startActivity(sendIntent)
            }
        }

        habitName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                saveHabitButton.isEnabled = !TextUtils.isEmpty(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        habitName.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
                habitName.error =
                    if (TextUtils.isEmpty((view as EditText).text)) getString(R.string.required_field_error) else null
        }

        habitRepetitions.addTextChangedListener(AfterTextChangedWatcher { setRepetitionLabel() })
        habitPeriodicity.addTextChangedListener(AfterTextChangedWatcher { setPeriodicityLabel() })

        fillForEdit()
        setRepetitionLabel()
        setPeriodicityLabel()
    }

    private fun setRepetitionLabel() {
        val pluralTimes = resources.getStringArray(R.array.times_plurals)
        val correctedTimes = TextHelpers.getPluralWord(getRepetitions(), pluralTimes)
        habitRepetitionLabel.text = getString(R.string.timesEvery, correctedTimes)
    }

    private fun setPeriodicityLabel() {
        val pluralDays = resources.getStringArray(R.array.days_plurals)
        val correctedPeriodicity = TextHelpers.getPluralWord(getPeriodicity(), pluralDays)
        habitPeriodicityLabel.text = correctedPeriodicity
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
        habitName.setText(habitToEdit.name)
        habitDescription.setText(habitToEdit.description)
        habitPriority.setSelection(habitToEdit.priority.value)
        habitTypeRadio.check(
            when (habitToEdit.type) {
                HabitType.Bad -> R.id.radio_bad
                HabitType.Good -> R.id.radio_good
            }
        )
        habitRepetitions.setText(habitToEdit.repetitions.toString())
        habitPeriodicity.setText(habitToEdit.periodicity.toString())
        habitPosition = intent.getIntExtra(EXTRA_HABIT_POSITION, -1)
    }

    private fun getHabit(): Habit {
        val name = habitName.text.toString()
        val description = getValueOrDefault(habitDescription, R.string.habitDescription)
        val priority = Priority.getByValue(habitPriority.selectedItemPosition)
        val type =
            when (habitTypeRadio.checkedRadioButtonId) {
                R.id.radio_bad -> HabitType.Bad
                R.id.radio_good -> HabitType.Good
                else -> throw Exception("You forgot to create new HabitType or handle it here")
            }

        val id = habitId ?: UUID.randomUUID()
        return Habit(name, description, priority, type, getRepetitions(), getPeriodicity(), id = id)
    }

    private fun getValueOrDefault(view: EditText, defaultResourceId: Int): String {
        return view.text.toString().let { if (TextUtils.isEmpty(it)) getString(defaultResourceId) else it }
    }

    private fun getRepetitions(): Int {
        return getValueOrDefault(habitRepetitions, R.string.numberHint).toInt()
    }

    private fun getPeriodicity(): Int {
        return getValueOrDefault(habitPeriodicity, R.string.numberHint).toInt()
    }
}
