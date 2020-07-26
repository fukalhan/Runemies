package cz.cvut.fukalhan.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.repository.entity.Route
import cz.cvut.fukalhan.shared.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationTrackingNotification(private val context: Context) : KoinComponent {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    private val observer: Observer<Route> = Observer { route ->
        distance += route.distance
        notificationLayout.setTextViewText(R.id.notification_distance, context.getString(R.string.notification_distance, String.format("%.2f", distance)))
        notificationBuilder.setCustomContentView(notificationLayout)
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, context.getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private var distance: Double = 0.0
    private var startTime: Long = 0
    private var pausedTime: Long = 0
    private lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notification: Notification
    private val notificationLayout = RemoteViews("cz.cvut.fukalhan", R.layout.notification_location_tracking)

    fun startNotifications() {
        startTime = SystemClock.elapsedRealtime()
        distance = 0.0
        notificationLayout.setChronometer(R.id.notification_timer, SystemClock.elapsedRealtime(), null, true)
        notificationLayout.setTextViewText(R.id.notification_distance, context.getString(R.string.notification_distance, String.format("%.2f", distance)))
        notificationBuilder = getNotificationBuilder()
        notification = notificationBuilder.build()
        notificationManager.notify(Constants.NOTIFICATION_ID, notification)
        locationTrackingRecord.record.observeForever(observer)
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val reopenActivity = PendingIntent.getActivity(context, 0, intent, 0)
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.runemies_icon))
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(reopenActivity)
    }

    fun pauseNotifications() {
        pausedTime = startTime - SystemClock.elapsedRealtime()
        notificationLayout.setChronometer(R.id.notification_timer, startTime, null, false)
        notificationBuilder.setCustomContentView(notificationLayout)
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
        locationTrackingRecord.record.removeObserver(observer)
    }

    fun continueNotifications() {
        startTime = SystemClock.elapsedRealtime() + pausedTime
        notificationLayout.setChronometer(R.id.notification_timer, SystemClock.elapsedRealtime() + pausedTime, null, true)
        notificationBuilder.setCustomContentView(notificationLayout)
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
        locationTrackingRecord.record.observeForever(observer)
    }

    fun stopNotifications() {
        locationTrackingRecord.record.removeObserver(observer)
        notificationManager.cancel(Constants.NOTIFICATION_ID)
    }
}