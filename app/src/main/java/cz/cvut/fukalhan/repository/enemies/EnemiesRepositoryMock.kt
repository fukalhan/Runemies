package cz.cvut.fukalhan.repository.enemies

import cz.cvut.fukalhan.repository.entity.User

class EnemiesRepositoryMock: IEnemiesRepository {
    override fun getEnemies(): List<User> {
        val enemies = ArrayList<User>()
        enemies.add(User(username = "Ilonka"))
        enemies.add(User(username = "Dominik"))
        enemies.add(User(username = "Narek"))
        enemies.add(User(username = "Vašek"))
        enemies.add(User(username = "Štěpánisko"))
        enemies.add(User(username = "Marek"))
        enemies.add(User(username = "Martina"))

        return enemies
    }

    override fun getEnemy(): User {
        TODO("Not yet implemented")
    }
}