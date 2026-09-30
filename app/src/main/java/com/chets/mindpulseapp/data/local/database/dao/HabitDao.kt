package com.chets.mindpulseapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chets.mindpulseapp.data.local.database.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY createAt DESC")
    fun getAllHabits() : Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit : HabitEntity): Long

    @Update
    suspend fun updateHabit(habit : HabitEntity)

    @Delete
    suspend fun deleteHabit(habit : HabitEntity)
}