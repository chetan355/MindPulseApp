package com.chets.mindpulseapp

import com.chets.mindpulseapp.data.local.database.entity.HabitEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HabitEntityTest {

    private val todayStr: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    @Test
    fun testDailyHabit_completedToday_returnsTrue() {
        val habit = HabitEntity(
            name = "Drink Water",
            icon = "Water",
            frequency = "DAILY",
            lastCompletedDate = todayStr
        )
        assertTrue(habit.isCompletedToday)
    }

    @Test
    fun testDailyHabit_completedYesterday_resetsAndReturnsFalse() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val habit = HabitEntity(
            name = "Drink Water",
            icon = "Water",
            frequency = "DAILY",
            lastCompletedDate = yesterdayStr
        )
        assertFalse(habit.isCompletedToday)
    }

    @Test
    fun testWeeklyHabit_completedEarlierThisWeek_returnsTrue() {
        val habit = HabitEntity(
            name = "Read 1 Book",
            icon = "Reading",
            frequency = "WEEKLY",
            lastCompletedDate = todayStr
        )
        assertTrue(habit.isCompletedToday)
    }

    @Test
    fun testWeeklyHabit_completedTwoWeeksAgo_returnsFalse() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, -2)
        val twoWeeksAgoStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val habit = HabitEntity(
            name = "Read 1 Book",
            icon = "Reading",
            frequency = "WEEKLY",
            lastCompletedDate = twoWeeksAgoStr
        )
        assertFalse(habit.isCompletedToday)
    }

    @Test
    fun testMonthlyHabit_completedEarlierThisMonth_returnsTrue() {
        val habit = HabitEntity(
            name = "Pay Rent",
            icon = "Health",
            frequency = "MONTHLY",
            lastCompletedDate = todayStr
        )
        assertTrue(habit.isCompletedToday)
    }

    @Test
    fun testMonthlyHabit_completedTwoMonthsAgo_returnsFalse() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -2)
        val twoMonthsAgoStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val habit = HabitEntity(
            name = "Pay Rent",
            icon = "Health",
            frequency = "MONTHLY",
            lastCompletedDate = twoMonthsAgoStr
        )
        assertFalse(habit.isCompletedToday)
    }

    @Test
    fun testIsDueToday_daily_returnsTrueAlways() {
        val habit = HabitEntity(
            name = "Exercise",
            icon = "Fitness",
            frequency = "DAILY"
        )
        assertTrue(habit.isDueToday)
    }
}
