package cz.cvut.fukalhan.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.Shared
import cz.cvut.fukalhan.utils.StringUtil
import org.greenrobot.eventbus.EventBus
import cz.cvut.fukalhan.R
import kotlin.math.roundToInt

class LocationTrackingService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var location: Location
    private var previousLocation: Location? = null
    private var distance: Double = 0.0
    private lateinit var notificationManager: NotificationManager
    private var requesting: Boolean = false
    private val notification: Notification
        get() {
            val text = StringUtil.getLocationText(location)
            val builder = NotificationCompat.Builder(this)
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

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        updateLastLocation()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /** On new location result received in location callback*/
    private fun onNewLocation(lastLocation: Location) {
        location = lastLocation

        if (previousLocation == null) {
            previousLocation = location
            distance = 0.0
            // Post new location to bus which updates UI in Run fragment
            EventBus.getDefault().postSticky(
                LocationChanged(location, distance)
            )
        } else {
            val addDistance = (location.distanceTo(previousLocation).roundToInt()) * 0.001
            previousLocation = location
            distance += addDistance
            // Post new location to bus which updates UI in Run fragment
            EventBus.getDefault().postSticky(
                LocationChanged(location, distance)
            )
        }

        // Add wayPoint to shared object
        Shared.wayPoints.add(lastLocation)

        // Update notification
        if (serviceIsRunningForeground(this)) {
            notificationManager.notify(Constants.NOTIFICATION_ID, notification)
        }
    }

    /** Determine if the service is running foreground */
    private fun serviceIsRunningForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    /** Set parameters for quality of location requests */
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = Constants.UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    /** Add listener on receiving location updates*/
    private fun updateLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        location = task.result!!
                    } else {
                        Log.e("LOC", "Failed to get location")
                    }
                }
        } catch (e: SecurityException) {
            Log.e("LOC", "No location permission$e")
        }
    }

    /** Start requesting location updates */
    fun requestLocationUpdates() {
        startService(Intent(applicationContext, LocationTrackingService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            requesting = true
        } catch (e: SecurityException) {
            Log.e("Loc", "Lost location permission$e")
        }
    }

    /** Stop requesting location updates */
    fun removeLocationUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            previousLocation = null
            distance = 0.0
            requesting = false
            stopSelf()
        } catch (e: SecurityException) {
            Log.e("Loc", "Lost Location permission")
        }
    }

    /** Bind service to Activity */
    private val binder: IBinder = LocalBinder()
    inner class LocalBinder : Binder() {
        val service: LocationTrackingService
            get() = this@LocationTrackingService
    }

    override fun onBind(intent: Intent): IBinder? {
        stopForeground(true)
        return binder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (requesting) {
            startForeground(Constants.NOTIFICATION_ID, notification)
        }
        return super.onUnbind(intent)
    }
}