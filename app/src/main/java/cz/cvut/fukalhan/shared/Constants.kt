package cz.cvut.fukalhan.shared

import android.Manifest

object Constants {
    const val USERS = "users"
    const val RUN_RECORDS = "runRecords"
    const val USER_RECORDS = "userRecords"
    const val DATE_PATTERN = "dd.MM.yyyy"
    const val UPDATE_INTERVAL: Long = 5000
    const val NOTIFICATION_ID = 1223
    const val EXTRA_STARTED_FROM_NOTIFICATION = "cz.cvut.fukalhan"
    const val CHANNEL_ID = "location_channel"

    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
}