package cz.cvut.fukalhan.shared

import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationTrackingRecord : KoinComponent {
    private val context: Context by inject()
    private val icon = DrawableToBitmapUtil.generateBitmapDescriptor(context, R.drawable.ic_map_marker)
    private var pathPoints: PolylineOptions = PolylineOptions().color(ContextCompat.getColor(context, R.color.green)) // .endCap(CustomCap(icon))
    val record: MutableLiveData<PolylineOptions> by lazy { MutableLiveData<PolylineOptions>() }

    fun updateRecord(location: Location) {
        addPathPoint(location)
        record.postValue(pathPoints)
    }

    private fun addPathPoint(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        pathPoints.add(coordinates)
    }
}