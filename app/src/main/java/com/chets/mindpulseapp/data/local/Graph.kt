package com.chets.mindpulseapp.data.local

import android.content.Context
import androidx.room.Room
import com.chets.mindpulseapp.data.local.database.MindPulseDatabase
import com.chets.mindpulseapp.data.local.repository.HabitRepository
import com.chets.mindpulseapp.data.local.repository.JournalRepository

object Graph {

    lateinit var mindPulseDatabase: MindPulseDatabase

    val journalRepository by lazy {

        JournalRepository(mindPulseDatabase.journalDao())

    }

    val habitRepository by lazy {
        HabitRepository(mindPulseDatabase.habitDao())
    }
    fun provide(context : Context){
        mindPulseDatabase = Room.databaseBuilder(context, MindPulseDatabase::class.java, "mindpulse.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

}