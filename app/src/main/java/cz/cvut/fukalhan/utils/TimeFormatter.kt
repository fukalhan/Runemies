package cz.cvut.fukalhan.utils

import android.annotation.SuppressLint
import cz.cvut.fukalhan.shared.Constants
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

object TimeFormatter {
    @SuppressLint("SimpleDateFormat")
    val simpleDate = SimpleDateFormat(Constants.DATE_PATTERN)

    fun toMinSec(timeInMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun toHourMinSec(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}