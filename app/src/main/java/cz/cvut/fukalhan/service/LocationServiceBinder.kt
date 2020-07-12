package cz.cvut.fukalhan.service

import android.os.Binder

class LocationServiceBinder(private val locationService: LocationService) : Binder() {
    val service: LocationService
        get() = locationService
}