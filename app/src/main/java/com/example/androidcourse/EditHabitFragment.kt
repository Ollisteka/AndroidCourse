package com.example.androidcourse

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.androidcourse.databinding.FragmentEditHabitBinding
import kotlinx.android.synthetic.main.fragment_edit_habit.*


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

        habitPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                model.priority = Priority.getByValue(position)
            }

        }

        habitTypeRadio.setOnCheckedChangeListener { _, checkedId ->
            model.type = when (checkedId) {
                R.id.radio_bad -> HabitType.Bad
                R.id.radio_good -> HabitType.Good
                else -> throw Exception("You forgot to create new HabitType or handle it here")
            }
        }

        setRepetitionLabel()
        setPeriodicityLabel()
    }

    fun update(habitToEdit: Habit) {
        model.update(habitToEdit)
        binding.invalidateAll()

        habitTypeRadio.check(
            when (model.type) {
                HabitType.Bad -> R.id.radio_bad
                HabitType.Good -> R.id.radio_good
            }
        )

        habitPriority.setSelection(model.priority.value)
    }


    fun getHabit(): Habit {
        return model.getHabit()
    }

    private fun setRepetitionLabel() {
        val pluralTimes = resources.getQuantityString(R.plurals.times, model.repetitions ?: getString(R.string.numberHint).toInt())
        habitRepetitionLabel.text = getString(R.string.timesEvery, pluralTimes)
    }

    private fun setPeriodicityLabel() {
        habitPeriodicityLabel.text = resources.getQuantityString(R.plurals.days, model.periodicity ?: getString(R.string.numberHint).toInt())
    }
}