package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fukalhan.repository.enemies.EnemiesFacade
import cz.cvut.fukalhan.repository.entity.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemiesViewModel: ViewModel(), KoinComponent {
    val enemies: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>()}
    private val enemiesFacade by inject<EnemiesFacade>()

    fun getEnemies() {
        val enemiesList = enemiesFacade.getEnemies()
        enemies.postValue(enemiesList)
    }
}