package com.example.androidcourse

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_habit.*
import java.util.*

class EditHabitFragment : Fragment() {
    private var habitId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                saveHabitButton.isEnabled = !TextUtils.isEmpty(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        habitName.onFocusChangeListener = View.OnFocusChangeListener { textView, hasFocus ->
            if (!hasFocus)
                habitName.error =
                    if (TextUtils.isEmpty((textView as EditText).text)) getString(
                        R.string.required_field_error
                    ) else null
        }

        habitRepetitions.addTextChangedListener(AfterTextChangedWatcher { setRepetitionLabel() })
        habitPeriodicity.addTextChangedListener(AfterTextChangedWatcher { setPeriodicityLabel() })

        setRepetitionLabel()
        setPeriodicityLabel()
    }

    fun update(habitToEdit: Habit) {
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
    }


    fun getHabit(): Habit {
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
}