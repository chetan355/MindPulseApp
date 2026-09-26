package com.chets.mindpulseapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val content : String,
    val createdAt : String,
    val updatedAt : String,
    val mood : String?,
    val isLocked : Boolean
)