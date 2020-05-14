package cz.cvut.fukalhan.repository.entity

import com.google.android.gms.maps.model.PolylineOptions

data class LocationChanged(
    val pathWay: PolylineOptions,
    val distance: Double,
    val currentPace: Long
)