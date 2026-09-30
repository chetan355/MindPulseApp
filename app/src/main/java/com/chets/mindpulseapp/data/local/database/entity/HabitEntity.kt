package com.chets.mindpulseapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    val createAt : Long = System.currentTimeMillis(),
    val isActive : Boolean = true,
    val frequency : String = "DAILY",
    val isCompletedToday : Boolean = false
)
