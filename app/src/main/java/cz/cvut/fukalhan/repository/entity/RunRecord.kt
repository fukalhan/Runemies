package cz.cvut.fukalhan.repository.entity

import com.google.android.gms.maps.model.LatLng

data class RunRecord(
    var id: String = "",
    val date: Long,
    val distance: Double,
    val time: Long,
    val pace: Long,
    val pathWay: List<LatLng>,
    var fastestKmTime: Long = 0
)