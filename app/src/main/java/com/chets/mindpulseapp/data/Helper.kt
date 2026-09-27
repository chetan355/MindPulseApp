package com.chets.mindpulseapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Helper {
    fun formatTimestamp(timestamp: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val messageDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
            val now = LocalDateTime.now()

            when {
                isSameDay(messageDateTime, now) -> "today ${formatTime(messageDateTime)}"
                isSameDay(messageDateTime.plusDays(1), now) -> "yesterday ${formatTime(messageDateTime)}"
                else -> formatDate(messageDateTime)
            }
        } else {
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isSameDay(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return dateTime1.format(formatter) == dateTime2.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return formatter.format(dateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        return formatter.format(dateTime)
    }
}
