package com.example.androidcourse.network

import com.example.androidcourse.core.EMPTY_UUID
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import com.example.androidcourse.database.CalendarConverter
import com.example.androidcourse.database.HabitTypeConverter
import com.example.androidcourse.database.PriorityConverter
import com.example.androidcourse.database.UUIDConverter
import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .registerTypeAdapter(Priority::class.java, PriorityAdapter())
    .registerTypeAdapter(HabitType::class.java, HabitTypeAdapter())
    .registerTypeHierarchyAdapter(Calendar::class.java, CalendarAdapter())
    .registerTypeHierarchyAdapter(UUID::class.java, UUIDAdapter())
    .create()

class PriorityAdapter : PriorityConverter(), JsonDeserializer<Priority>, JsonSerializer<Priority> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Priority {
        return toPriority(json?.asInt ?: 0)
    }

    override fun serialize(src: Priority?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(fromPriority(src ?: Priority.Low))
    }
}

class HabitTypeAdapter : HabitTypeConverter(), JsonDeserializer<HabitType>, JsonSerializer<HabitType> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HabitType {
        return toHabitType(json?.asInt ?: 0);
    }

    override fun serialize(src: HabitType?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(fromHabitType(src ?: HabitType.Good))
    }
}

class CalendarAdapter : CalendarConverter(), JsonDeserializer<Calendar>, JsonSerializer<Calendar> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Calendar {
        return fromTimestamp(json?.asLong) ?: Calendar.getInstance()
    }

    override fun serialize(src: Calendar, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(toTimestamp(src))
    }
}

class UUIDAdapter : UUIDConverter(), JsonDeserializer<UUID>, JsonSerializer<UUID> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): UUID {
        return if (json == null) UUID.randomUUID() else toUUID(json.asString)
    }

    override fun serialize(src: UUID?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        // todo выкинуть этот костыль
        if (src == null || src == EMPTY_UUID)
            return JsonNull.INSTANCE
        return JsonPrimitive(fromUUID(src))
    }
}