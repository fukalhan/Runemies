package cz.cvut.fukalhan.shared

import android.content.Context
import android.location.Location
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.repository.entity.RunRecord
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Singleton of this class is shared by location tracking service and run fragment
 * for communication between them through observable live data,
 * service inserts location updates and run fragment observes for changes
 */
class LocationTrackingRecord : KoinComponent {
    private val context: Context by inject()
    val locationChanged: MutableLiveData<LocationChanged> by lazy { MutableLiveData<LocationChanged>() }
    val record: MutableLiveData<RunRecord> by lazy { MutableLiveData<RunRecord>() }
    private val pathPoints = ArrayList<Location>()
    private var polylineOptions: PolylineOptions = PolylineOptions().color(ContextCompat.getColor(context, R.color.green))
    private var distance: Double = 0.0
    private var startTime: Long = 0
    private var pace: Long = 0

    /** Set all records to initial state */
    fun resetRecords() {
        pathPoints.clear()
        polylineOptions = PolylineOptions().color(ContextCompat.getColor(context, R.color.green))
        distance = 0.0
        startTime = SystemClock.elapsedRealtime()
        pace = 0
    }

    /** Update records and post that location has changed */
    fun newLocation(location: Location) {
        updateRecords(location)
        locationChanged.postValue(LocationChanged(polylineOptions, distance, pace))
    }

    /** Updates records according to new location */
    private fun updateRecords(location: Location) {
        // If there is any previous location saved
        if (pathPoints.isNotEmpty()) {
            // Add distance between new location and last location saved in pathPoints to overall distance
            distance += location.distanceTo(pathPoints.last()).roundToInt() * 0.001
        }

        // The pace of the run is in milliseconds on kilometer
        pace = SystemClock.elapsedRealtime() - startTime
        if (distance != 0.0) {
            pace = (pace / distance).roundToLong()
        }

        pathPoints.add(location)
        // Add location to be drawn by polyline
        polylineOptions.add(LatLng(location.latitude, location.longitude))
    }

    /** Post final result of location tracking results after the run is ended */
    fun postResult() {
        record.postValue(RunRecord(
            date = SystemClock.elapsedRealtime(),
            distance = distance,
            time = SystemClock.elapsedRealtime() - startTime,
            pace = pace,
            pathWay = pathPoints
        ))
    }
}