package cz.cvut.fukalhan.repository.entity

data class ActivityStatistics(
    val totalMileage: Double = 0.0,
    val totalTime: Long = 0,
    val longestRun: Double = 0.0
)