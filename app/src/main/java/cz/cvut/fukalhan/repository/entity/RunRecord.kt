package cz.cvut.fukalhan.repository.entity

import android.location.Location

data class RunRecord(
    var id: String = "",
    var date: Long = 0,
    var distance: Double = 0.0,
    var time: Long = 0,
    var pace: Long = 0,
    val pathWay: List<Location>,
    var fastestKmTime: Long = 0
)