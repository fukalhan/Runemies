package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fukalhan.repository.enemies.EnemiesFacade
import cz.cvut.fukalhan.repository.entity.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class SingleEnemyViewModel: ViewModel(), KoinComponent {
    val enemy: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val enemiesFacade by inject<EnemiesFacade>()


    fun getEnemy(id: String) {
        val singleEnemy = enemiesFacade.getEnemy(id)
        enemy.postValue(singleEnemy)
    }
}