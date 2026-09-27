package com.chets.mindpulseapp.data.local.repository

import com.chets.mindpulseapp.data.local.database.dao.JournalDao
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao)
{
    fun getAllEntries() : Flow<List<JournalEntity>>
    {
        return journalDao.getAllEntries()
    }

    fun getJournalEntry(id : Long): Flow<JournalEntity>
    {
        return journalDao.getEntry(id)
    }

    suspend fun saveEntry(journalEntity: JournalEntity){
        journalDao.insert(journalEntity)
    }

    suspend fun updateEntry(journalEntity: JournalEntity){
        journalDao.update(journalEntity)
    }

    suspend fun delete(journalEntity: JournalEntity)
    {
        journalDao.delete(journalEntity)
    }
}
