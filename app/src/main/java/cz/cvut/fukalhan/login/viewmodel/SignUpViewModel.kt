package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.states.SignUpState
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignUpViewModel : ViewModel(), KoinComponent {
    val signUpState: MutableLiveData<SignUpState> by lazy { MutableLiveData<SignUpState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            val regState = loginFacade.signUpUser(username, email, password)
            signUpState.postValue(regState)
        }
    }
}