package cz.cvut.fukalhan.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.shared.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationTrackingNotification(private val context: Context) : KoinComponent {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val lifecycleOwner = context as LifecycleOwner
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, context.getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private var distance: Double = 0.0
    private lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notification: Notification

    fun startNotifications() {
        distance = 0.0
        notificationBuilder = getNotificationBuilder()
        notification = notificationBuilder.build()
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
        locationTrackingRecord.record.observe(lifecycleOwner, Observer { route ->
            notificationBuilder.setContentText("Distance: ${distance + route.distance}")
            notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
        })
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val reopenActivity = PendingIntent.getActivity(context, 0, intent, 0)
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setContentTitle("Run")
            .setContentText("Distance: $distance")
            .setOnlyAlertOnce(true)
            .setUsesChronometer(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_flash)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(reopenActivity)
    }

    fun stopNotifications() {
        notificationManager.cancel(Constants.NOTIFICATION_ID)
    }
}