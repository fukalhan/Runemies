package cz.cvut.fukalhan.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.LoginFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel : ViewModel(), KoinComponent {
    val signOutState: MutableLiveData<SignOutState> by lazy { MutableLiveData<SignOutState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signOutUser() {
        viewModelScope.launch {
            val signOut = loginFacade.signOutUser()
            signOutState.postValue(signOut)
        }
    }
}