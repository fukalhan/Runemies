package cz.cvut.fukalhan.common

interface LocationTracking {
    fun startTracking()
    fun pauseTracking()
    fun continueTracking()
    fun stopTracking()
}