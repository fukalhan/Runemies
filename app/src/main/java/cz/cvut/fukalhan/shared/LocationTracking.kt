package cz.cvut.fukalhan.shared

import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

object LocationTracking: KoinComponent {
    private val context: Context by inject()
    private val icon = DrawableToBitmapUtil.generateBitmapDescriptor(context, R.drawable.ic_map_marker)
    var pathPoints = PolylineOptions().color(ContextCompat.getColor(context, R.color.green)).endCap(CustomCap(icon))

    fun addPathPoint(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        pathPoints.add(coordinates)
    }
}