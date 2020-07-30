package cz.cvut.fukalhan.repository.entity

import com.google.android.gms.maps.model.LatLng

data class RunRecord(
    var date: Long = 0,
    var distance: Double = 0.0,
    var time: Long = 0,
    var pace: Long = 0,
    var pathWay: List<LatLng> = emptyList(),
    var id: String = ""
)