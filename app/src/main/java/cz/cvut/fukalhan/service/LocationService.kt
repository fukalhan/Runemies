package cz.cvut.fukalhan.service

import android.app.Service
import android.app.NotificationManager
import android.app.NotificationChannel
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
import org.greenrobot.eventbus.EventBus
import org.koin.core.KoinComponent

class LocationService : Service(), KoinComponent {
    private val binder: IBinder = LocationServiceBinder(this)
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: Handler
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var lastLocation: Location
    private lateinit var notification: LocationTrackingNotificationBuilder
    private lateinit var notificationManager: NotificationManager

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

        notification =
            LocationTrackingNotificationBuilder(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, getString(R.string.appName), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /** On new location result received in location callback*/
    private fun onNewLocation(location: Location) {
        lastLocation = location
        EventBus.getDefault().post(location)
        notificationManager.notify(Constants.NOTIFICATION_ID, notification.build(location))
    }

    /** Set parameters for quality of location requests */
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = Constants.UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun updateLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result!!
                } else {
                    Log.e("LOC", "Failed to get location")
                }
            }
        } catch (e: SecurityException) {
            Log.e("LOC", "No location permission$e")
        }
    }

    fun getLastLocation(): Location? {
        return if (this::lastLocation.isInitialized)
            lastLocation
        else
            null
    }

    /** Start requesting location updates */
    fun startLocationTracking() {
        startService(Intent(applicationContext, LocationService::class.java))
    }

    /** After service is started these actions will be triggered */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler.post {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
            } catch (e: SecurityException) {
                Log.e("Loc", "Lost location permission$e")
            }
        }
        return START_STICKY
    }

    /** Stop requesting location updates */
    fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }
}