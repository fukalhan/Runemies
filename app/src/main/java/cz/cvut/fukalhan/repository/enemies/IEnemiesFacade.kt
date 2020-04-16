package cz.cvut.fukalhan.repository.enemies

import cz.cvut.fukalhan.repository.entity.User

interface IEnemiesFacade {
    fun getEnemies(): List<User>

    fun getEnemy(id: String): User
}