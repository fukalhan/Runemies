package cz.cvut.fukalhan.service

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.repository.entity.Route
import org.koin.core.KoinComponent
import kotlin.math.roundToInt

class LocationTrackingRecord : KoinComponent {
    val record: MutableLiveData<Route> by lazy { MutableLiveData<Route>() }
    private var distance: Double = 0.0
    private var pathWay: List<LatLng> = emptyList()

    fun reset() {
        distance = 0.0
        pathWay = emptyList()
    }

    fun onNewLocation(newLocation: Location) {
        record.postValue(updateRecord(newLocation))
    }

    private fun updateRecord(location: Location): Route {
        val newLocation = LatLng(location.latitude, location.longitude)
        if (pathWay.isNotEmpty()) {
            val previousLocation = pathWay.last()
            val result = FloatArray(1)
            Location.distanceBetween(
                previousLocation.latitude,
                previousLocation.longitude,
                newLocation.latitude,
                newLocation.longitude,
                result
            )
            distance += (result[0] * 0.1).roundToInt() * 0.01
        }
        pathWay = pathWay + newLocation

        return Route(distance, pathWay)
    }
}