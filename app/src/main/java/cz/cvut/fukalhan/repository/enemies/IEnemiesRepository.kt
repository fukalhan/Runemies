package cz.cvut.fukalhan.repository.enemies

import cz.cvut.fukalhan.repository.entity.User

interface IEnemiesRepository {
    fun getEnemies(): List<User>

    fun getEnemy(): User
}