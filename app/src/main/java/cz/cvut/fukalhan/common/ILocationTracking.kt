package cz.cvut.fukalhan.common

interface ILocationTracking {
    fun startTracking()
    fun pauseTracking()
    fun continueTracking()
    fun stopTracking()
}