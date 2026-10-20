package com.chets.mindpulseapp.data

import com.chets.mindpulseapp.data.local.database.entity.JournalEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DailyMoodPoint(
    val dateLabel: String,
    val score: Float,
    val moodObject: MoodObjects?,
    val entryCount: Int,
    val timestamp: Long
)

data class InsightsSummary(
    val averageScore: Float,
    val averageMood: MoodObjects?,
    val totalEntries: Int,
    val predominantMood: MoodObjects?
)

object InsightsHelper {

    fun getFilteredEntries(entries: List<JournalEntity>, isWeekly: Boolean): List<JournalEntity> {
        val days = if (isWeekly) 7 else 30
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        val cutoffTimestamp = cal.timeInMillis

        return entries.filter { it.createdAt >= cutoffTimestamp }
    }

    fun getDailyMoodPoints(entries: List<JournalEntity>, isWeekly: Boolean): List<DailyMoodPoint> {
        val dayCount = if (isWeekly) 7 else 30
        val points = mutableListOf<DailyMoodPoint>()
        val dateFormat = if (isWeekly) SimpleDateFormat("EEE", Locale.getDefault()) else SimpleDateFormat("d", Locale.getDefault())
        val dayKeyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val daysList = mutableListOf<Date>()

        for (i in (dayCount - 1) downTo 0) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -i)
            daysList.add(c.time)
        }

        val entriesByDay = entries.groupBy { dayKeyFormat.format(Date(it.createdAt)) }

        daysList.forEach { date ->
            val dayKey = dayKeyFormat.format(date)
            val dayEntries = entriesByDay[dayKey] ?: emptyList()

            val scores = dayEntries.mapNotNull { entry ->
                moodList.firstOrNull { it.mood.equals(entry.mood, ignoreCase = true) }?.score?.toFloat()
            }

            val avgScore = if (scores.isNotEmpty()) scores.average().toFloat() else 0f
            val closestMood = getClosestMood(avgScore)

            points.add(
                DailyMoodPoint(
                    dateLabel = dateFormat.format(date),
                    score = avgScore,
                    moodObject = closestMood,
                    entryCount = dayEntries.size,
                    timestamp = date.time
                )
            )
        }

        return points
    }

    fun getInsightsSummary(entries: List<JournalEntity>): InsightsSummary {
        val validScores = entries.mapNotNull { entry ->
            moodList.firstOrNull { it.mood.equals(entry.mood, ignoreCase = true) }
        }

        if (validScores.isEmpty()) {
            return InsightsSummary(
                averageScore = 0f,
                averageMood = null,
                totalEntries = 0,
                predominantMood = null
            )
        }

        val avgScore = validScores.map { it.score }.average().toFloat()
        val closestMood = getClosestMood(avgScore)

        val predominantMood = validScores
            .groupBy { it }
            .maxByOrNull { it.value.size }
            ?.key

        return InsightsSummary(
            averageScore = avgScore,
            averageMood = closestMood,
            totalEntries = entries.size,
            predominantMood = predominantMood
        )
    }

    private fun getClosestMood(score: Float): MoodObjects? {
        if (score <= 0f) return null
        return moodList.minByOrNull { kotlin.math.abs(it.score - score) }
    }
}
