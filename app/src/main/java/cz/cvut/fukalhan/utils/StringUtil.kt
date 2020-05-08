package cz.cvut.fukalhan.utils

import android.location.Location
import java.text.DateFormat
import java.util.Date

object StringUtil {
    val locationText: String
        get() = String.format("Location updated: %1\$s", DateFormat.getInstance().format(Date()))

    fun getLocationText(location: Location?): String {
        return if (location == null) "Unknown Location" else "${location.latitude}/${location.longitude}"
    }
}