package cz.cvut.fukalhan.main.profile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileViewModel: ViewModel(), KoinComponent {
    val user: MutableLiveData<User?> by lazy { MutableLiveData<User?>() }
    private val userFacade by inject<UserFacade>()

    fun getUser(id: String) {
        viewModelScope.launch {
            val userData = userFacade.getUser(id)
            user.postValue(userData)
        }
    }

}