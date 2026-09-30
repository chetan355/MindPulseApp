package com.chets.mindpulseapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chets.mindpulseapp.data.local.database.dao.HabitDao
import com.chets.mindpulseapp.data.local.database.dao.JournalDao
import com.chets.mindpulseapp.data.local.database.entity.HabitEntity
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity

@Database(
    entities = [JournalEntity::class, HabitEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MindPulseDatabase : RoomDatabase()
{
    abstract fun journalDao() : JournalDao
    abstract fun habitDao(): HabitDao
}
