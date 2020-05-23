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
import com.example.androidcourse.showToast
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

        habitName.onFocusChangeListener = View.OnFocusChangeListener(requiredTextFieldHandler())
        habitDescription.onFocusChangeListener = View.OnFocusChangeListener(requiredTextFieldHandler())

        habitName.doAfterTextChanged { _ ->
            saveHabitButton.isEnabled = model.requiredFieldsFilled()
        }
        habitDescription.doAfterTextChanged { _ ->
            saveHabitButton.isEnabled = model.requiredFieldsFilled()
        }

        habitRepetitions.doAfterTextChanged {
            setRepetitionLabel()
            saveHabitButton.isEnabled = model.requiredFieldsFilled()
        }
        habitPeriodicity.doAfterTextChanged {
            setPeriodicityLabel()
            saveHabitButton.isEnabled = model.requiredFieldsFilled()
        }
        habitPriority.onItemSelectedListener = model.priorityUpdater
        saveHabitButton.isEnabled = model.requiredFieldsFilled()

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
        model.habit.type = habitType
        update()
    }

    fun update(habitId: UUID) {
        model.update(habitId).invokeOnCompletion { update() }
    }

    private fun update() {
        binding.invalidateAll()

        habitTypeRadio.check(
            when (model.habit.type) {
                HabitType.Bad -> R.id.radio_bad
                HabitType.Good -> R.id.radio_good
            }
        )

        habitPriority.setSelection(model.habit.priority.value)
    }

    suspend fun saveHabit(): Boolean {
        view?.post {
            saveHabitButton.isEnabled = false
            saveHabitButton.text = resources.getString(R.string.save_inProcess)
        }
        val isSaved = model.saveHabit()
        if (!isSaved) {
            view?.post {
                saveHabitButton.isEnabled = true
                saveHabitButton.text = resources.getString(R.string.save)
                showToast(context, R.string.error_save)
                showToast(context, R.string.error_network)
            }
        }
        return isSaved
    }

    private fun setRepetitionLabel() {
        val pluralTimes = resources.getQuantityString(
            R.plurals.times, model.habit.repetitions ?: getString(
                R.string.numberHint
            ).toInt()
        )
        habitRepetitionLabel.text = getString(R.string.timesEvery, pluralTimes)
    }

    private fun setPeriodicityLabel() {
        habitPeriodicityLabel.text = resources.getQuantityString(
            R.plurals.days, model.habit.periodicity ?: getString(
                R.string.numberHint
            ).toInt()
        )
    }
}