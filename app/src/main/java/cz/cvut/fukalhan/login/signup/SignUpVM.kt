package cz.cvut.fukalhan.login.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.state.SignUpState
import kotlinx.coroutines.launch

class SignUpVM(private val loginFacade: LoginFacade) : ViewModel() {
    val signUpState: MutableLiveData<SignUpState> by lazy { MutableLiveData<SignUpState>() }

    fun signUp(signUpData: SignUpData) {
        viewModelScope.launch {
            val regState = loginFacade.signUpUser(signUpData)
            signUpState.postValue(regState)
        }
    }
}