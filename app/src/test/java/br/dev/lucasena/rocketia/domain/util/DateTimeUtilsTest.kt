package br.dev.lucasena.rocketia.domain.util

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale
import kotlin.math.exp

class DateTimeUtilsTest {
    @Test
    fun `GIVEN time in millis equals 0 WHEN format THEN should return empty string`() {
        val dummyDateTimeInMillis = 0L
        val result = dummyDateTimeInMillis.formatDateTime()
        assertEquals("", result)
    }

    @Test
    fun `GIVEN time in millis higher than 0 WHEN format THEN should return datetime string`() {
        val dummyLocal = Locale.forLanguageTag("pt-BR")
        val dummyDateTimeInMillis = 1756295438711L
        val expectedDateTimeText = "27/08/2025 08:50"

        val result = dummyDateTimeInMillis.formatDateTime(locale = dummyLocal)
        assertEquals(expectedDateTimeText, result)
    }

    @Test
    fun `GIVEN time in millis higher than 0 WHEN format THEN should return time string`() {
        val dummyLocal = Locale.forLanguageTag("pt-BR")
        val dummyDateTimeInMillis = 1756295438711L
        val expectedDateTimeText = "08:50"

        val result = dummyDateTimeInMillis.formatTime(locale = dummyLocal)
        assertEquals(expectedDateTimeText, result)
    }
}