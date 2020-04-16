package cz.cvut.fukalhan.repository.enemies

import cz.cvut.fukalhan.repository.entity.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemiesFacade: IEnemiesFacade, KoinComponent {
    val repository by inject<EnemiesRepository>()
    val repostioryMock by inject<EnemiesRepositoryMock>()

    override fun getEnemies(): List<User> {
        return repostioryMock.getEnemies()
    }

    override fun getEnemy(): User {
        TODO("Not yet implemented")
    }
}