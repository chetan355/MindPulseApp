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
    var currentEntryId by mutableStateOf(0L)
    var currentCreatedAt by mutableStateOf(0L)
    var journalContent by mutableStateOf("")
    var selectedMood by mutableStateOf<String?>(null)

    val isEditing: Boolean get() = currentEntryId != 0L

    fun onJournalContentChange(content : String){
        journalContent = content
    }

    fun onMoodChange(mood: String?) {
        selectedMood = mood
    }

    fun setEditingEntry(entry: JournalEntity) {
        currentEntryId = entry.id
        currentCreatedAt = entry.createdAt
        journalContent = entry.content
        selectedMood = entry.mood
    }

    fun resetState() {
        currentEntryId = 0L
        currentCreatedAt = 0L
        journalContent = ""
        selectedMood = null
    }

    val getAllJournalEntries : Flow<List<JournalEntity>> = journalRepository.getAllEntries()

    fun getJournalEntry(id : Long) : Flow<JournalEntity>
    {
        return journalRepository.getJournalEntry(id)
    }

    fun saveJournalEntry(content: String, mood: String?) {
        viewModelScope.launch {
            val timestamp = if (currentCreatedAt != 0L) currentCreatedAt else System.currentTimeMillis()
            val entity = JournalEntity(
                id = currentEntryId,
                content = content,
                createdAt = timestamp,
                mood = mood
            )
            journalRepository.saveEntry(entity)
            resetState()
        }
    }

    fun addJournalEntry(journalEntry : JournalEntity)
    {
        viewModelScope.launch {
            journalRepository.saveEntry(journalEntry)
            resetState()
        }
    }

    fun deleteCurrentEntry() {
        if (currentEntryId != 0L) {
            viewModelScope.launch {
                val entity = JournalEntity(
                    id = currentEntryId,
                    content = journalContent,
                    createdAt = currentCreatedAt,
                    mood = selectedMood
                )
                journalRepository.delete(entity)
                resetState()
            }
        }
    }

    fun deleteJournalEntry(journalEntity: JournalEntity){
        viewModelScope.launch {
            journalRepository.delete(journalEntity)
            resetState()
        }
    }
}
