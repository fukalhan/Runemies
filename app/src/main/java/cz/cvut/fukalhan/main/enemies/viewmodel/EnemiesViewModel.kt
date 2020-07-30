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

class EnemiesViewModel : ViewModel(), KoinComponent {
    private val userFacade by inject<UserFacade>()
    val enemies: MutableLiveData<DataWrapper<ArrayList<User>>> by lazy {
        MutableLiveData<DataWrapper<ArrayList<User>>>()
            .also {
                viewModelScope.launch {
                    val users = userFacade.getUsers()
                    users.data?.sortWith(EnemiesComparator())
                    enemies.postValue(users)
                }
            }
    }
}