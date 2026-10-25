package com.chets.mindpulseapp.di

import android.content.Context
import androidx.room.Room
import com.chets.mindpulseapp.data.local.database.MindPulseDatabase
import com.chets.mindpulseapp.data.local.database.dao.HabitDao
import com.chets.mindpulseapp.data.local.database.dao.JournalDao
import com.chets.mindpulseapp.data.local.repository.HabitRepository
import com.chets.mindpulseapp.data.local.repository.JournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMindPulseDatabase(
        @ApplicationContext context: Context
    ): MindPulseDatabase {
        return Room.databaseBuilder(
            context,
            MindPulseDatabase::class.java,
            "mindpulse.db"
        )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }

    @Provides
    @Singleton
    fun provideJournalDao(database: MindPulseDatabase): JournalDao {
        return database.journalDao()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: MindPulseDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideJournalRepository(journalDao: JournalDao): JournalRepository {
        return JournalRepository(journalDao)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository {
        return HabitRepository(habitDao)
    }
}
