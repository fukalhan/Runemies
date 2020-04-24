package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemiesViewModel: ViewModel(), KoinComponent {
    val enemies: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>()}
    private val userFacade by inject<UserFacade>()

    fun getEnemies(currentUserID: String) {
        viewModelScope.launch {
            val users = userFacade.getUsers()
            enemies.postValue(filterCurrentUser(currentUserID, users))
        }
    }

    private fun filterCurrentUser (currentUserID: String, users: List<User>): List<User> {
        val enemies = ArrayList<User>()
        users.forEach { user ->
            if (user.id != currentUserID) {
                enemies.add(user)
            }
        }
        return enemies
    }
}