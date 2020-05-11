package cz.cvut.fukalhan.shared

import android.location.Location

object LocationTrackingRecords {
    val wayPoints = ArrayList<Location>()
    var distance: Double = 0.0
    var tempo: Long = 0
}