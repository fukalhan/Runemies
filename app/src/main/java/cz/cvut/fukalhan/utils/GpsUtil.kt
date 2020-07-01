package cz.cvut.fukalhan.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import cz.cvut.fukalhan.common.IOnGpsListener
import cz.cvut.fukalhan.shared.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Determine if GPS is turned on in the device,
 * if yes, change set gpsStatus in IOnGpsListener to true,
 * if not, make dialog for user to turn the gps on
 */
object GpsUtil : KoinComponent {
    private val appContext: Context by inject()
    private val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val settingsClient = LocationServices.getSettingsClient(appContext)

    private val locationRequest =
        LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(Constants.UPDATE_INTERVAL)

    private val locationSettingsRequest =
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true).build()

    fun turnGpsOn(onGpsListener: IOnGpsListener, context: Context) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener.gpsStatus(true)
        } else {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    onGpsListener.gpsStatus(true)
                }
                .addOnFailureListener { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context as Activity, Constants.GPS_REQUEST)
                            } catch (e: IntentSender.SendIntentException) {
                                Log.e(TAG, "PendingIntent unable to execute request.")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate and cannot be fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
}