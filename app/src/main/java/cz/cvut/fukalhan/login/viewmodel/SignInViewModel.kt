package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.UserLogin
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignInViewModel: ViewModel(), KoinComponent {
    val signInState : MutableLiveData<SignInState> by lazy { MutableLiveData<SignInState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signIn (email: String, password: String) {
        val userLogin = UserLogin(email, password)
        viewModelScope.launch {
            val signIn = loginFacade.signInUser(userLogin)
            signInState.postValue(signIn)
        }
    }


}