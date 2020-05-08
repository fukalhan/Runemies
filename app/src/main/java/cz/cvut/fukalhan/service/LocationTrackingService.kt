package cz.cvut.fukalhan.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.shared.Constants
import org.greenrobot.eventbus.EventBus
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.service.notification.LocationTrackingNotificationBuilder
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class LocationTrackingService : Service(), KoinComponent {
    private val appContext: Context by inject()
    private val binder: IBinder = LocationTrackingServiceBinder()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var location: Location
    private var previousLocation: Location? = null
    private var distance: Double = 0.0
    private var time: Long = 0
    private var tempo: Long = 0
    private lateinit var notificationBuilder: LocationTrackingNotificationBuilder
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

        notificationBuilder = LocationTrackingNotificationBuilder(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, resources.getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /** On new location result received in location callback*/
    private fun onNewLocation(lastLocation: Location) {
        location = lastLocation
        if (previousLocation != null) {
            val distanceBetween = location.distanceTo(previousLocation).roundToInt()
            tempo = (1000/distanceBetween)*(System.currentTimeMillis() - time)
            time = System.currentTimeMillis()
            distance += distanceBetween * 0.001
        }
        previousLocation = location

        // Post new location to bus which updates UI in Run fragment
        EventBus.getDefault().postSticky(
            LocationChanged(location, distance, tempo)
        )

        // Update notification
        if (serviceIsRunningForeground(this)) {
            notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build(location))
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
    fun startLocationTracking(context: Context) {
        startService(Intent(context, LocationTrackingService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            requesting = true
            previousLocation = null
            time = System.currentTimeMillis()
            distance = 0.0
            tempo = 0
        } catch (e: SecurityException) {
            Log.e("Loc", "Lost location permission$e")
        }
    }

    /** Stop requesting location updates */
    fun stopLocationTracking() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            EventBus.getDefault().postSticky(
                RunRecord(distance = distance)
            )
            previousLocation = null
            distance = 0.0
            time = 0
            tempo = 0
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

    override fun onUnbind(intent: Intent): Boolean {
        if (requesting) {
            startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build(location))
        }
        return super.onUnbind(intent)
    }
}