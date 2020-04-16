package cz.cvut.fukalhan.repository.enemies

import cz.cvut.fukalhan.repository.entity.User

class EnemiesRepositoryMock: IEnemiesRepository {
    val enemies: ArrayList<User> = arrayListOf(
    User(id = "1-Ilonka", username = "Ilonka"),
    User(id = "2-Dominik", username = "Dominik"),
    User(id = "3-Narek", username = "Narek"),
    User(id = "4-Vašek", username = "Vašek"),
    User(id = "5-Štěpánisko", username = "Štěpánisko"),
    User(id = "6-Marek", username = "Marek"),
    User(id = "7-Martina", username = "Martina")
    )
    override fun getEnemies(): List<User> {
        return enemies
    }

    override fun getEnemy(id: String): User {
        enemies.forEach{
         if (it.id == id) { return it }
        }
        return User(username = "Nobody")
    }
}