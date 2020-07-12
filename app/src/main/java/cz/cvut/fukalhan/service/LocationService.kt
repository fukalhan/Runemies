package cz.cvut.fukalhan.service

import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationResult
import cz.cvut.fukalhan.shared.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationService : LifecycleService(), KoinComponent {
    private val binder: IBinder = LocationServiceBinder(this)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: Handler
    private lateinit var locationTrackingNotification: LocationTrackingNotification

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationTrackingRecord.onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()

        handlerThread = HandlerThread("locationTracking")
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)

        locationTrackingNotification = LocationTrackingNotification(this)
    }

    /** Set parameters for quality of location requests */
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = Constants.UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /** Start location service */
    fun startLocationTracking() {
        startService(Intent(applicationContext, LocationService::class.java))
    }

    /** Callback after service is started, start requesting location updates */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        serviceHandler.post {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
            } catch (e: SecurityException) {
                Log.e("LocationService", "Lost location permission$e")
            }
        }
        locationTrackingRecord.reset()
        locationTrackingNotification.startNotifications()
        startForeground(Constants.NOTIFICATION_ID, locationTrackingNotification.notification)
        return START_STICKY
    }

    fun pauseLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        locationTrackingNotification.pauseNotifications()
    }

    fun continueLocationTracking() {
        locationTrackingRecord.reset()
        serviceHandler.post {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
            } catch (e: SecurityException) {
                Log.e("LocationService", "Lost location permission$e")
            }
        }
        locationTrackingNotification.continueNotifications()
    }

    /** Stop requesting location updates */
    fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        locationTrackingNotification.stopNotifications()
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        handlerThread.quit()
        super.onDestroy()
    }
}