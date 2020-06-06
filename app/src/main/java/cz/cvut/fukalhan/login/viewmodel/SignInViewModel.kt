package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/** Communicate to login facade and set the result to signInState observable */
class SignInViewModel : ViewModel(), KoinComponent {
    val signInState: MutableLiveData<SignInState> by lazy { MutableLiveData<SignInState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val signIn = loginFacade.signInUser(email, password)
            signInState.postValue(signIn)
        }
    }
}