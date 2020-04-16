package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fukalhan.repository.entity.User

class SingleEnemyViewModel: ViewModel() {
    val enemy: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    fun getEnemy() {

    }
}