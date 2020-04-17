package com.example.androidcourse.database

import androidx.room.TypeConverter
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import java.util.*

open class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.value
    }

    @TypeConverter
    fun toPriority(priority: Int): Priority {
        return Priority.getByValue(priority)
    }
}

open class HabitTypeConverter {
    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int {
        return habitType.value
    }

    @TypeConverter
    fun toHabitType(habitType: Int): HabitType {
        return HabitType.getByValue(habitType)
    }
}

open class CalendarConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let {
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = it
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}

open class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}