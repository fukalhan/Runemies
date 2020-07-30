package cz.cvut.fukalhan.repository.entity

import com.google.android.gms.maps.model.LatLng

data class Route(
    var distance: Int = 0,
    var newLocation: LatLng
)