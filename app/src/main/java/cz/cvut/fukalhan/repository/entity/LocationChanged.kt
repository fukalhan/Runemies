package cz.cvut.fukalhan.repository.entity

import android.location.Location

data class LocationChanged(
    var location: Location,
    var distance: Double,
    var tempo: Long
)