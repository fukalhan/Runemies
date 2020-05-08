package cz.cvut.fukalhan.service

import android.os.Binder

class LocationTrackingServiceBinder(private val locationTrackingService: LocationTrackingService) : Binder() {
    val service: LocationTrackingService
        get() = locationTrackingService
}