package cz.cvut.fukalhan.service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.StringUtil

class LocationTrackingNotificationBuilder(private val context: Context) {
    fun build(location: Location): Notification {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val reopenActivity = PendingIntent.getActivity(context, 0, intent, 0)
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setContentTitle(StringUtil.locationText)
            .setContentText(StringUtil.getLocationText(location))
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_flash)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(reopenActivity)
            .setWhen(System.currentTimeMillis())
            .build()
    }
}