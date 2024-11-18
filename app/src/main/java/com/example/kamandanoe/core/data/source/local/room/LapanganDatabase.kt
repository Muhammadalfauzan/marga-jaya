package com.example.kamandanoe.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kamandanoe.core.data.source.local.entity.Converter
import com.example.kamandanoe.core.data.source.local.entity.LapanganEntity

@Database(entities = [LapanganEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class LapanganDatabase : RoomDatabase() {

    abstract fun lapDao(): LapanganDao

}