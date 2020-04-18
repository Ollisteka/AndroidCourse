package com.example.androidcourse.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.androidcourse.R
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.databinding.FragmentEditHabitBinding
import com.example.androidcourse.viewmodels.EditableHabitViewModel
import kotlinx.android.synthetic.main.fragment_edit_habit.*
import java.util.*


class EditHabitFragment : Fragment() {
    private val model: EditableHabitViewModel by viewModels()
    private lateinit var binding: FragmentEditHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_habit, container, false
        )
        binding.model = model
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var nameFilled = false
        var descriptionFilled = false

        habitName.onFocusChangeListener = View.OnFocusChangeListener(requiredTextFieldHandler())
        habitDescription.onFocusChangeListener = View.OnFocusChangeListener(requiredTextFieldHandler())

        habitName.doAfterTextChanged { text ->
            nameFilled = !TextUtils.isEmpty(text)
            saveHabitButton.isEnabled = nameFilled && descriptionFilled
        }
        habitDescription.doAfterTextChanged { text ->
            descriptionFilled = !TextUtils.isEmpty(text)
            saveHabitButton.isEnabled = nameFilled && descriptionFilled
        }

        habitRepetitions.doAfterTextChanged { setRepetitionLabel() }
        habitPeriodicity.doAfterTextChanged { setPeriodicityLabel() }
        habitPriority.onItemSelectedListener = model.priorityUpdater

        setRepetitionLabel()
        setPeriodicityLabel()
    }

    private fun requiredTextFieldHandler(): (textView: View, hasFocus: Boolean) -> Unit {
        return { textView, hasFocus ->
            if (!hasFocus)
                (textView as EditText).error =
                    if (TextUtils.isEmpty(textView.text)) getString(
                        R.string.required_field_error
                    ) else null
        }
    }

    fun update(habitType: HabitType) {
        model.type = habitType
        update()
    }

    fun update(habitId: UUID) {
        model.update(habitId).invokeOnCompletion { update() }
    }

    private fun update() {
        binding.invalidateAll()

        habitTypeRadio.check(
            when (model.type) {
                HabitType.Bad -> R.id.radio_bad
                HabitType.Good -> R.id.radio_good
            }
        )

        habitPriority.setSelection(model.priority.value)
    }

    fun saveHabit() = model.saveHabit()

    private fun setRepetitionLabel() {
        val pluralTimes = resources.getQuantityString(
            R.plurals.times, model.repetitions ?: getString(
                R.string.numberHint
            ).toInt()
        )
        habitRepetitionLabel.text = getString(R.string.timesEvery, pluralTimes)
    }

    private fun setPeriodicityLabel() {
        habitPeriodicityLabel.text = resources.getQuantityString(
            R.plurals.days, model.periodicity ?: getString(
                R.string.numberHint
            ).toInt()
        )
    }
}