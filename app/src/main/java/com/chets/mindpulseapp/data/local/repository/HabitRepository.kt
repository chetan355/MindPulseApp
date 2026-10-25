package com.chets.mindpulseapp.data.local.repository

import com.chets.mindpulseapp.data.local.database.dao.HabitDao
import com.chets.mindpulseapp.data.local.database.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {

    fun getAllHabits() : Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }

    suspend fun insertHabit(habitEntity: HabitEntity){
        habitDao.insertHabit(habitEntity)
    }

    suspend fun updateHabit(habitEntity: HabitEntity){
        habitDao.updateHabit(habitEntity)
    }

    suspend fun deleteHabit(habitEntity: HabitEntity){
        habitDao.deleteHabit(habitEntity)
    }
}