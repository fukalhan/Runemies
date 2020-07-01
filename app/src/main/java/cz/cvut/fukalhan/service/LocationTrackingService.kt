package cz.cvut.fukalhan.service

import android.app.Service
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationResult
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.service.notification.LocationTrackingNotificationBuilder
import cz.cvut.fukalhan.shared.LocationTrackingRecord
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationTrackingService : Service(), KoinComponent {
    private val binder: IBinder = LocationTrackingServiceBinder(this)
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: Handler
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var location: Location
    private lateinit var notification: LocationTrackingNotificationBuilder
    private lateinit var notificationManager: NotificationManager
    private var requesting: Boolean = false

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

        handlerThread = HandlerThread("locationTracking")
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)

        notification = LocationTrackingNotificationBuilder(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /** On new location result received in location callback*/
    private fun onNewLocation(lastLocation: Location) {
        location = lastLocation
        locationTrackingRecord.newLocation(location)

        // TODO don't show notifications when app is no longer in background
        // Update notification
        if (serviceIsRunningForeground(this)) {
            notificationManager.notify(Constants.NOTIFICATION_ID, notification.build(location))
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
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /** Get last known location */
    fun getLocation(): Location? {
        if (this::location.isInitialized) {
            return location
        } else {
            return null
        }
    }

    // TODO get rid of this, check if permissions are still given in one place
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
    fun startLocationTracking() {
        startService(Intent(applicationContext, LocationTrackingService::class.java))
    }

    /** After service is started these actions will be triggered */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler.post {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
                requesting = true
                resetTrackingRecords()
            } catch (e: SecurityException) {
                Log.e("Loc", "Lost location permission$e")
            }
        }
        return START_STICKY
    }

    /** Resets all the records in location tracking record*/
    private fun resetTrackingRecords() {
        locationTrackingRecord.resetRecords()
    }

    /** Stop requesting location updates */
    fun stopLocationTracking() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            requesting = false
            locationTrackingRecord.postResult()
            stopSelf()
        } catch (e: SecurityException) {
            Log.e("Loc", "Lost Location permission")
        }
    }

    fun removeNotifications() {
        stopForeground(true)
    }

    fun startNotifications() {
        if (requesting) {
            startForeground(Constants.NOTIFICATION_ID, notification.build(location))
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (requesting) {
            startForeground(Constants.NOTIFICATION_ID, notification.build(location))
        }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }
}