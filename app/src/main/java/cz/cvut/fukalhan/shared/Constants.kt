package cz.cvut.fukalhan.shared

import android.Manifest

object Constants {
    const val USERS = "users"
    const val RUN_RECORDS = "runRecords"
    const val USER_RECORDS = "userRecords"
    const val CHALLENGES = "challenges"
    const val DATE_PATTERN = "dd.MM.yyyy"
    const val UPDATE_INTERVAL: Long = 5000
    const val NOTIFICATION_ID = 1223
    const val CHANNEL_ID = "location_channel"
    const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    const val GPS_REQUEST = 42
    const val PROFILE_IMAGE_PATH = "profileImages/"

    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
}