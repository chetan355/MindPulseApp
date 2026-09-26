package com.chets.mindpulseapp.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.chets.mindpulseapp.R

sealed class MoodObjects(val mood : String, @DrawableRes val emoji : Int, val tint : Color)
{
    object veryLow : MoodObjects(
        "Very Low",
        R.drawable.outline_sentiment_sad_24,
        Color(0xFFE53935) // Red
    )

    object low : MoodObjects(
        "Low",
        R.drawable.outline_sentiment_very_dissatisfied_24,
        Color(0xFFFF8A80) // Light Red
    )

    object okay : MoodObjects(
        "Okay",
        R.drawable.outline_sentiment_satisfied_24,
        Color(0xFFFFB74D) // Amber / Orange
    )
    object good : MoodObjects(
        "Good",
        R.drawable.outline_mood_24,
        Color(0xFF81C784) // Light Green
    )
    object great : MoodObjects(
        "Great",
        R.drawable.outline_sentiment_very_satisfied_24,
        Color(0xFF4CAF50) // Green
    )
}

val moodList = listOf<MoodObjects>(
    MoodObjects.veryLow,
    MoodObjects.low,
    MoodObjects.okay,
    MoodObjects.good,
    MoodObjects.great
)