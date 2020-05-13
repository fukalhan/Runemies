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
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.shared.Constants
import org.greenrobot.eventbus.EventBus
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.service.notification.LocationTrackingNotificationBuilder
import cz.cvut.fukalhan.shared.LocationTracking
import kotlin.math.roundToInt

class LocationTrackingService : Service() {
    private val binder: IBinder = LocationTrackingServiceBinder(this)
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: Handler
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var location: Location
    private var previousLocation: Location? = null
    private var distance: Double = 0.0
    private var time: Long = 0
    private var tempo: Long = 0
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
        if (previousLocation != null) {
            val distanceBetween = location.distanceTo(previousLocation).roundToInt()
            // If distance between previous and current location isn't zero,
            // we get tempo in milliseconds on km
            if (distanceBetween != 0) {
                tempo = (1000 / distanceBetween) * (System.currentTimeMillis() - time)
            }
            distance += distanceBetween / 1000
        }
        previousLocation = location
        time = System.currentTimeMillis()

        LocationTracking.addPathPoint(location)

        // Post new location to bus which updates UI in Run fragment
        EventBus.getDefault().postSticky(
            LocationChanged(location, distance, tempo)
        )

        // TODO nezobrazovat notifikace když ještě netrackujeme trasu nebo když není appka v pozadí
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
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    /** Get last known location */
    fun getLocation(): Location {
        return location
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler.post {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
                requesting = true
                previousLocation = null
                time = System.currentTimeMillis()
                distance = 0.0
                tempo = 0
            } catch (e: SecurityException) {
                Log.e("Loc", "Lost location permission$e")
            }
        }
        return START_STICKY
    }

    fun resetRecords() {
        time = System.currentTimeMillis()
        distance = 0.0
        tempo = 0
    }

    /** Stop requesting location updates */
    fun stopLocationTracking() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            requesting = false
            stopSelf()
        } catch (e: SecurityException) {
            Log.e("Loc", "Lost Location permission")
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        stopForeground(true)
        return binder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        super.onRebind(intent)
    }

    fun goForeground() {
        if (requesting) {
            startForeground(Constants.NOTIFICATION_ID, notification.build(location))
        }
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