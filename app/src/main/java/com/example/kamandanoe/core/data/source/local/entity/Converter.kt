package com.example.kamandanoe.core.data.source.local.entity

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")
    }
}
