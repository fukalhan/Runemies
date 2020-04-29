package cz.cvut.fukalhan.repository.entity

data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var joinDate: Long = 0,
    var lives: Int = 3,
    var points: Int = 0,
    var totalMileage: Double = 0.0,
    var totalTime: Double = 0.0,
    var longestRun: Double = 0.0,
    var fastest1km: Long = 0
)