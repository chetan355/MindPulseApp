package com.chets.mindpulseapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao
{
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllEntries() : Flow<List<JournalEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getEntry(id : Long) : Flow<JournalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry : JournalEntity): Long

    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)
}
