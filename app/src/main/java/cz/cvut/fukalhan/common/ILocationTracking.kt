package cz.cvut.fukalhan.common

import android.location.Location

interface ILocationTracking {
    fun startTracking()
    fun stopTracking()
    fun resetRecords()
    fun getLastLocation(): Location?
}