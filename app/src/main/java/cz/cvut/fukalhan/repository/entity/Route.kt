package cz.cvut.fukalhan.repository.entity

import com.google.android.gms.maps.model.LatLng

data class Route(
    var distance: Double = 0.0,
    var pathPoints: List<LatLng> = emptyList()
)