package cz.cvut.fukalhan.repository.entity

data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var joinDate: Long = 0,
    var points: Int = 0
)