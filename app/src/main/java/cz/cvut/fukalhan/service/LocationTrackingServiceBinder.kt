package cz.cvut.fukalhan.service

import android.os.Binder

class LocationTrackingServiceBinder: Binder() {
    val service: LocationTrackingService
        get() = LocationTrackingService()
}