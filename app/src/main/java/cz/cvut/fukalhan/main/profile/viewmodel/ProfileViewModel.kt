package cz.cvut.fukalhan.main.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileViewModel : ViewModel(), KoinComponent {
    val user: MutableLiveData<DataWrapper<User>> by lazy { MutableLiveData<DataWrapper<User>>() }
    private val userFacade by inject<UserFacade>()

    fun getUser(userId: String) {
        viewModelScope.launch {
            val userData = userFacade.getUser(userId)
            user.postValue(userData)
        }
    }
}