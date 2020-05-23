package com.example.androidcourse.viewmodels

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcourse.R
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import com.example.androidcourse.database.HabitsDatabase
import com.example.androidcourse.network.HabitDto
import com.example.androidcourse.network.HabitMapper
import com.example.androidcourse.network.service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class EditableHabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    var habit: HabitDto = HabitDto()

    var stringRepetitions: String
        get() = habit.repetitions?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != habit.repetitions)
                habit.repetitions = int
        }

    var stringPeriodicity: String
        get() = habit.periodicity?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != habit.periodicity)
                habit.periodicity = int
        }

    fun setType(checkedRadioId: Int) {
        habit.type = when (checkedRadioId) {
            R.id.radio_bad -> HabitType.Bad
            R.id.radio_good -> HabitType.Good
            else -> throw Exception("You forgot to create new HabitType or handle it here")
        }
    }

    fun update(habitId: UUID): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            val task = async(Dispatchers.IO) { habitsDao?.findById(habitId) }
            val habit = task.await()
            if (habit != null)
                updateUi(habit)
        }
    }

    private fun updateUi(habit: Habit) {
        this.habit.name = habit.name
        this.habit.description = habit.description
        this.habit.priority = habit.priority
        this.habit.type = habit.type
        this.habit.repetitions = habit.repetitions
        this.habit.periodicity = habit.periodicity
        this.habit.color = habit.color
        this.habit.id = habit.id
    }

    suspend fun saveHabit(): Boolean {
        val uidDto = service.addOrUpdateHabit(habit) ?: return false

        habit.id = uidDto.uid;
        habitsDao?.upsert(HabitMapper.fromDto(habit))
        return true
    }

    val priorityUpdater = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position != habit.priority.value)
                habit.priority = Priority.getByValue(position)
        }
    }

    fun requiredFieldsFilled() =
        !TextUtils.isEmpty(habit.name) && !TextUtils.isEmpty(habit.description) && habit.repetitions != null && habit.periodicity != null
}