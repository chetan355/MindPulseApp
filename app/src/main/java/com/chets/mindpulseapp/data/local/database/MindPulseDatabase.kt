package com.chets.mindpulseapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chets.mindpulseapp.data.local.database.dao.JournalDao
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity

@Database(
    entities = [JournalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MindPulseDatabase : RoomDatabase()
{
    abstract fun journalDao() : JournalDao
}
