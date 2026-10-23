package com.chets.mindpulseapp

import com.chets.mindpulseapp.data.Helper
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HelperTest {

    @Test
    fun testGetGreeting_returnsValidGreeting() {
        val greeting = Helper.getGreeting()
        assertNotNull(greeting)
        assertTrue(
            greeting == "Good Morning!" ||
            greeting == "Good Afternoon!" ||
            greeting == "Good Evening!" ||
            greeting == "Good Night!"
        )
    }

    @Test
    fun testFormatTimestamp_returnsFormattedString() {
        val now = System.currentTimeMillis()
        val formatted = Helper.formatTimestamp(now)
        assertNotNull(formatted)
        assertTrue(formatted.isNotEmpty())
    }
}
