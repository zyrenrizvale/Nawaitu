package com.nawaitu.app.data.local

import androidx.room.TypeConverter
import com.nawaitu.app.data.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = try {
        Priority.valueOf(value)
    } catch (e: IllegalArgumentException) {
        Priority.MEDIUM
    }
}
