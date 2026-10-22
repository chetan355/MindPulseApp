package com.chets.mindpulseapp.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AiAnalysisManager {

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-3.8-flash",
            apiKey = com.chets.mindpulseapp.BuildConfig.GEMINI_API_KEY
        )
    }

    suspend fun generateInsight(
        summary: InsightsSummary,
        isWeekly: Boolean,
        recentExcerpts: List<String>
    ): String = withContext(Dispatchers.IO) {
        val timeframe = if (isWeekly) "this week" else "this month"

        if (summary.totalEntries == 0) {
            return@withContext "No journal entries recorded for $timeframe yet. Start writing reflections to unlock AI insights into your emotional well-being!"
        }

        val prompt = buildPrompt(summary, timeframe, recentExcerpts)

        try {
            val response = generativeModel.generateContent(prompt)
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                return@withContext text
            }
        } catch (e: Exception) {
            Log.d("GEMINI",e.toString())
            // Fallback to local intelligent analysis generator on offline / API unavailable
        }

        return@withContext generateLocalAnalysis(summary, timeframe)
    }

    private fun buildPrompt(
        summary: InsightsSummary,
        timeframe: String,
        excerpts: List<String>
    ): String {
        val moodName = summary.averageMood?.mood ?: "Okay"
        val predominantName = summary.predominantMood?.mood ?: "Okay"
        val sampleText = excerpts.take(3).joinToString("; ")

        return """
            Act as a compassionate, supportive wellness AI companion for an app named MindPulse.
            Analyze the user's emotional pattern for $timeframe:
            - Average Mood: $moodName (score ${String.format(java.util.Locale.getDefault(), "%.1f", summary.averageScore)} out of 5)
            - Most Frequent Mood: $predominantName
            - Total Journal Reflections Logged: ${summary.totalEntries}
            - Recent Excerpts: "$sampleText"
            
            Write a brief, warm, 2-3 sentence personalized insight message. Encourage their self-reflection habit and highlight positive emotional trends or offer gentle encouragement.
        """.trimIndent()
    }

    private fun generateLocalAnalysis(summary: InsightsSummary, timeframe: String): String {
        val avgScore = summary.averageScore
        val count = summary.totalEntries
        val predominant = summary.predominantMood?.mood ?: "Okay"

        return when {
            avgScore >= 4.0f ->
                "You've had a remarkably uplifting period $timeframe with $count reflection${if (count > 1) "s" else ""}! Your mood has predominantly been $predominant. Keep nurturing the habits and moments that bring you joy."

            avgScore >= 3.0f ->
                "Your emotional state $timeframe has been balanced and steady across $count journal entry${if (count > 1) "ies" else ""}. You're maintaining consistency in tracking your thoughts—steady progress!"

            avgScore > 0f ->
                "It looks like $timeframe has brought some challenging moments, but logging $count reflection${if (count > 1) "s" else ""} shows great mindfulness. Be gentle with yourself and take time to rest."

            else ->
                "Keep logging your daily thoughts $timeframe to track your emotional growth and build deeper self-awareness!"
        }
    }
}
