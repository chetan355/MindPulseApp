package com.chets.mindpulseapp

import com.chets.mindpulseapp.data.InsightsHelper
import com.chets.mindpulseapp.data.MoodObjects
import com.chets.mindpulseapp.data.local.database.entity.JournalEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar

class InsightsHelperTest {

    @Test
    fun testGetFilteredEntries_weekly_filtersCorrectly() {
        val now = System.currentTimeMillis()
        val sixDaysAgo = now - (6 * 86400000L)
        val tenDaysAgo = now - (10 * 86400000L)

        val entries = listOf(
            JournalEntity(id = 1, content = "Recent 1", createdAt = now, mood = "Good"),
            JournalEntity(id = 2, content = "Recent 2", createdAt = sixDaysAgo, mood = "Great"),
            JournalEntity(id = 3, content = "Old 1", createdAt = tenDaysAgo, mood = "Okay")
        )

        val weeklyFiltered = InsightsHelper.getFilteredEntries(entries, isWeekly = true)
        assertEquals(2, weeklyFiltered.size)
    }

    @Test
    fun testGetFilteredEntries_monthly_filtersCorrectly() {
        val now = System.currentTimeMillis()
        val twentyDaysAgo = now - (20 * 86400000L)
        val fortyDaysAgo = now - (40 * 86400000L)

        val entries = listOf(
            JournalEntity(id = 1, content = "Entry 1", createdAt = now, mood = "Good"),
            JournalEntity(id = 2, content = "Entry 2", createdAt = twentyDaysAgo, mood = "Okay"),
            JournalEntity(id = 3, content = "Entry 3", createdAt = fortyDaysAgo, mood = "Low")
        )

        val monthlyFiltered = InsightsHelper.getFilteredEntries(entries, isWeekly = false)
        assertEquals(2, monthlyFiltered.size)
    }

    @Test
    fun testDailyMoodPoints_multipleEntriesSameDay_averagesScoresCorrectly() {
        val now = System.currentTimeMillis()

        // 3 entries today: "Good" (4), "Great" (5), "Okay" (3) -> Average: (4 + 5 + 3) / 3 = 4.0
        val entries = listOf(
            JournalEntity(id = 1, content = "Morning entry", createdAt = now, mood = "Good"),
            JournalEntity(id = 2, content = "Afternoon entry", createdAt = now, mood = "Great"),
            JournalEntity(id = 3, content = "Evening entry", createdAt = now, mood = "Okay")
        )

        val summary = InsightsHelper.getInsightsSummary(entries)
        assertEquals(4.0f, summary.averageScore, 0.01f)
        assertNotNull(summary.averageMood)
        assertEquals("Good", summary.averageMood?.mood)
    }

    @Test
    fun testDailyMoodPoints_twoEntriesSameDay_averagesScoresCorrectly() {
        val now = System.currentTimeMillis()

        // 2 entries: "Low" (2) + "Okay" (3) -> Average: 2.5
        val entries = listOf(
            JournalEntity(id = 1, content = "Entry A", createdAt = now, mood = "Low"),
            JournalEntity(id = 2, content = "Entry B", createdAt = now, mood = "Okay")
        )

        val summary = InsightsHelper.getInsightsSummary(entries)
        assertEquals(2.5f, summary.averageScore, 0.01f)
    }

    @Test
    fun testGetInsightsSummary_emptyEntries_returnsZero() {
        val summary = InsightsHelper.getInsightsSummary(emptyList())
        assertEquals(0f, summary.averageScore, 0.01f)
        assertNull(summary.averageMood)
        assertEquals(0, summary.totalEntries)
        assertNull(summary.predominantMood)
    }

    @Test
    fun testGetInsightsSummary_predominantMood_identifiesMostFrequent() {
        val now = System.currentTimeMillis()
        val entries = listOf(
            JournalEntity(id = 1, content = "A", createdAt = now, mood = "Great"),
            JournalEntity(id = 2, content = "B", createdAt = now, mood = "Great"),
            JournalEntity(id = 3, content = "C", createdAt = now, mood = "Low")
        )

        val summary = InsightsHelper.getInsightsSummary(entries)
        assertEquals("Great", summary.predominantMood?.mood)
    }
}
