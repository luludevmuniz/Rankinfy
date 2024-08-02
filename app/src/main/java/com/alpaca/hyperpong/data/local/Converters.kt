package com.alpaca.hyperpong.data.local

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String {
        return value?.toString() ?: ""
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime? {
        return if (value.isEmpty()) null else LocalDateTime.parse(value)
    }
}