package com.chets.mindpulseapp

import com.chets.mindpulseapp.data.AiAnalysisManager
import com.chets.mindpulseapp.data.InsightsSummary
import com.chets.mindpulseapp.data.MoodObjects
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AiAnalysisManagerTest {

    @Before
    fun setUp() {
        AiAnalysisManager.forceLocalAnalysisForTesting = true
    }

    @Test
    fun testGenerateInsight_emptyEntries_returnsPromptToJournal() = runTest {
        val emptySummary = InsightsSummary(
            averageScore = 0f,
            averageMood = null,
            totalEntries = 0,
            predominantMood = null
        )

        val result = AiAnalysisManager.generateInsight(
            summary = emptySummary,
            isWeekly = true,
            recentExcerpts = emptyList()
        )

        assertNotNull(result)
        assertTrue(result.contains("No journal entries recorded"))
    }

    @Test
    fun testGenerateInsight_highMoodSummary_generatesUpliftingInsight() = runTest {
        val highSummary = InsightsSummary(
            averageScore = 4.5f,
            averageMood = MoodObjects.great,
            totalEntries = 5,
            predominantMood = MoodObjects.great
        )

        val result = AiAnalysisManager.generateInsight(
            summary = highSummary,
            isWeekly = true,
            recentExcerpts = listOf("Felt great", "Awesome day")
        )

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testGenerateInsight_lowMoodSummary_generatesEmpatheticInsight() = runTest {
        val lowSummary = InsightsSummary(
            averageScore = 2.0f,
            averageMood = MoodObjects.low,
            totalEntries = 3,
            predominantMood = MoodObjects.low
        )

        val result = AiAnalysisManager.generateInsight(
            summary = lowSummary,
            isWeekly = false,
            recentExcerpts = listOf("Feeling down", "Tough day")
        )

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }
}
