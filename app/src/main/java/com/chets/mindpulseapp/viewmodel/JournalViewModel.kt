package com.chets.mindpulseapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chets.mindpulseapp.data.local.Graph
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity
import com.chets.mindpulseapp.data.local.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JournalViewModel(private val journalRepository: JournalRepository = Graph.journalRepository) : ViewModel()
{

    var journalContent by mutableStateOf("")

    fun onJournalContentChange(content : String){
        journalContent = content
    }

    val getAllJournalEntries : Flow<List<JournalEntity>> = journalRepository.getAllEntries()

    fun getJournalEntry(id : Long) : Flow<JournalEntity>
    {
        return journalRepository.getJournalEntry(id)
    }

    fun addJournalEntry(journalEntry : JournalEntity)
    {
        viewModelScope.launch {
            journalRepository.saveEntry(journalEntry)
        }
    }

    fun deleteJournalEntry(journalEntity: JournalEntity){
        viewModelScope.launch {
            journalRepository.delete(journalEntity)
        }
    }
}
