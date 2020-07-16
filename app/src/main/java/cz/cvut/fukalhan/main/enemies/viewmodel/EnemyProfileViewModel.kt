package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemyProfileViewModel : ViewModel(), KoinComponent {
    val enemy: MutableLiveData<DataWrapper<User>> by lazy { MutableLiveData<DataWrapper<User>>() }
    private val userFacade by inject<UserFacade>()

    fun getEnemy(userId: String) {
        viewModelScope.launch {
            enemy.postValue(userFacade.getUser(userId))
        }
    }
}