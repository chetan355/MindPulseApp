package com.chets.mindpulseapp.data.local

import android.content.Context
import androidx.room.Room
import com.chets.mindpulseapp.data.local.database.MindPulseDatabase
import com.chets.mindpulseapp.data.local.repository.JournalRepository

object Graph {

    lateinit var mindPulseDatabase: MindPulseDatabase

    val journalRepository by lazy {

        JournalRepository(mindPulseDatabase.journalDao())

    }

    fun provide(context : Context){
        mindPulseDatabase = Room.databaseBuilder(context,MindPulseDatabase::class.java,"mindpulse.db").build()
    }

}