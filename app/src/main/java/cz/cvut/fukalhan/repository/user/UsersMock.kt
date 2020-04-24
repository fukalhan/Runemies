package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User

object UsersMock {
    val users: ArrayList<User> = arrayListOf(
        User(id = "1-Ilonka", username = "Ilonka"),
        User(id = "2-Dominik", username = "Dominik"),
        User(id = "3-Narek", username = "Narek"),
        User(id = "4-Vašek", username = "Vašek"),
        User(id = "5-Štěpánisko", username = "Štěpánisko"),
        User(id = "6-Marek", username = "Marek"),
        User(id = "7-Martina", username = "Martina")
    )
}