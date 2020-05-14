package cz.cvut.fukalhan.timeformattertest

import cz.cvut.fukalhan.common.TimeFormatter
import org.junit.Test
import org.junit.Assert.assertEquals

class TimeFormatterTest {
    @Test
    fun millisToMinAndSecConversion() {
        assertEquals("00:05", TimeFormatter.toMinSec(5000))
        assertEquals("16:40", TimeFormatter.toMinSec(1000000))
    }
}