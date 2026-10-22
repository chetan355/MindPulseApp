package com.chets.mindpulseapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    val createAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val frequency: String = "DAILY",
    val lastCompletedDate: String = ""
) {
    val isCompletedToday: Boolean
        get() {
            if (lastCompletedDate.isEmpty()) return false
            val locale = Locale.getDefault()
            val calToday = Calendar.getInstance()
            val calLast = Calendar.getInstance()

            return try {
                val sdfDay = SimpleDateFormat("yyyy-MM-dd", locale)
                val lastDate = sdfDay.parse(lastCompletedDate) ?: return false
                calLast.time = lastDate

                when (frequency.uppercase(locale)) {
                    "WEEKLY" -> {
                        val sameYear = calToday.get(Calendar.YEAR) == calLast.get(Calendar.YEAR)
                        val sameWeek = calToday.get(Calendar.WEEK_OF_YEAR) == calLast.get(Calendar.WEEK_OF_YEAR)
                        sameYear && sameWeek
                    }
                    "MONTHLY" -> {
                        val sameYear = calToday.get(Calendar.YEAR) == calLast.get(Calendar.YEAR)
                        val sameMonth = calToday.get(Calendar.MONTH) == calLast.get(Calendar.MONTH)
                        sameYear && sameMonth
                    }
                    else -> {
                        val todayStr = sdfDay.format(calToday.time)
                        lastCompletedDate == todayStr
                    }
                }
            } catch (e: Exception) {
                false
            }
        }

    val isDueToday: Boolean
        get() {
            val calToday = Calendar.getInstance()
            val dayOfWeek = calToday.get(Calendar.DAY_OF_WEEK)
            return when (frequency.uppercase(Locale.getDefault())) {
                "WEEKDAYS" -> dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY
                "WEEKENDS" -> dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                else -> true
            }
        }
}
