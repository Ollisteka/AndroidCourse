import androidx.lifecycle.ViewModel
import com.example.androidcourse.Habit
import com.example.androidcourse.HabitType
import java.util.*


class HabitsViewModel : ViewModel() {
    private val habits: MutableList<Habit> = mutableListOf(
        Habit("Хорошая", "Описание", type = HabitType.Good),
        Habit("Плохая", "Описание", type = HabitType.Bad)
    )

    fun getHabits(habitType: HabitType): List<Habit> {
        return habits.filter { it.type == habitType }
    }

    fun addOrUpdate(newHabit: Habit) {
        val existingHabit = getIndexedHabit(newHabit.id)
        if (existingHabit != null) {
            habits[existingHabit.index] = newHabit
        } else {
            habits.add(newHabit)
        }
    }

    fun findById(id: UUID): Habit? {
        return getIndexedHabit(id)?.value
    }

    private fun getIndexedHabit(id: UUID) = habits.withIndex().find { it.value.id == id }
}