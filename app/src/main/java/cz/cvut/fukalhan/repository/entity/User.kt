package cz.cvut.fukalhan.repository.entity

data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var lives: Int = 3,
    var points: Int = 0,
    var joinDate: Long = 0
)