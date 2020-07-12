package cz.cvut.fukalhan.main.run.viewmodel

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.shared.Constants

class LocationUpdate(context: Context) {
    val location: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>() }
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            location.postValue(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude))
        }
    }
    private val locationRequest = LocationRequest().setInterval(Constants.UPDATE_INTERVAL).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    fun startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } catch (e: SecurityException) {
            Log.e("LocationService", "Lost location permission$e")
        }
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}