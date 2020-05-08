package cz.cvut.fukalhan.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.core.app.NotificationCompat
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.StringUtil

class LocationTrackingNotificationBuilder(private val context: Context) {
    fun build(location: Location): Notification {
        val text = StringUtil.getLocationText(location)
        val builder = NotificationCompat.Builder(context)
            .setContentTitle(StringUtil.locationText)
            .setContentText(text)
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setWhen(System.currentTimeMillis())
        // Set the channel id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(Constants.CHANNEL_ID)
        }
        return builder.build()
    }
}