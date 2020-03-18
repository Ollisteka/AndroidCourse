package com.example.androidcourse

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
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

        habitName.doAfterTextChanged { text -> saveHabitButton.isEnabled = !TextUtils.isEmpty(text) }
        habitName.onFocusChangeListener = View.OnFocusChangeListener { textView, hasFocus ->
            if (!hasFocus)
                habitName.error =
                    if (TextUtils.isEmpty((textView as EditText).text)) getString(
                        R.string.required_field_error
                    ) else null
        }

        habitRepetitions.doAfterTextChanged { setRepetitionLabel() }
        habitPeriodicity.doAfterTextChanged { setPeriodicityLabel() }

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
        val pluralTimes = resources.getQuantityString(R.plurals.times, getRepetitions())
        habitRepetitionLabel.text = getString(R.string.timesEvery, pluralTimes)
    }

    private fun setPeriodicityLabel() {
        habitPeriodicityLabel.text = resources.getQuantityString(R.plurals.days, getPeriodicity())
    }
}