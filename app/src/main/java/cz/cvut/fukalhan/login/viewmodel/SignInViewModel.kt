package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.states.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignInViewModel : ViewModel(), KoinComponent {
    val signInState: MutableLiveData<SignInState> by lazy { MutableLiveData<SignInState>() }
    val newNewPasswordSentState: MutableLiveData<NewPasswordSentState> by lazy { MutableLiveData<NewPasswordSentState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val signIn = loginFacade.signInUser(email, password)
            signInState.postValue(signIn)
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            newNewPasswordSentState.postValue(loginFacade.sendNewPasswordEmail(email))
        }
    }
}