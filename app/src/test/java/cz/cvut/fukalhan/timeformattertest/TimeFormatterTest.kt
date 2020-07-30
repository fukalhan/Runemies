package cz.cvut.fukalhan.timeformattertest

import cz.cvut.fukalhan.utils.TimeFormatter
import org.junit.Test
import org.junit.Assert.assertEquals

class TimeFormatterTest {
    @Test
    fun millisToMinSecConversion() {
        assertEquals("00:05", TimeFormatter.toMinSec(5000))
        assertEquals("16:40", TimeFormatter.toMinSec(1000000))
    }

    @Test
    fun millisToHourMinSecConversion() {
        assertEquals("00:00:05", TimeFormatter.toHourMinSec(5000))
        assertEquals("00:16:40", TimeFormatter.toHourMinSec(1000000))
        assertEquals("01:16:40", TimeFormatter.toHourMinSec(4600000))
    }
}