package com.chets.mindpulseapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chets.mindpulseapp.data.local.Graph
import com.chets.mindpulseapp.data.local.database.entity.HabitEntity
import com.chets.mindpulseapp.data.local.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitViewModel(private val habitRepository: HabitRepository = Graph.habitRepository) : ViewModel()
{
    val getAllHabits : Flow<List<HabitEntity>> = habitRepository.getAllHabits()

    fun saveHabit(habitEntity: HabitEntity)
    {
        viewModelScope.launch {
            habitRepository.insertHabit(habitEntity)
        }
    }

    fun toggleHabitCompletion(habitEntity: HabitEntity) {
        viewModelScope.launch {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val newDate = if (habitEntity.isCompletedToday) "" else todayStr
            val updated = habitEntity.copy(lastCompletedDate = newDate)
            habitRepository.insertHabit(updated)
        }
    }

    fun deleteHabit(habitEntity: HabitEntity)
    {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitEntity)
        }
    }
}