package cz.cvut.fukalhan.login.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.state.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.state.SignInState
import kotlinx.coroutines.launch

class SignInVM(private val loginFacade: LoginFacade) : ViewModel() {
    val signInState: MutableLiveData<SignInState> by lazy { MutableLiveData<SignInState>() }
    val newPasswordSentState: MutableLiveData<NewPasswordSentState> by lazy { MutableLiveData<NewPasswordSentState>() }

    fun signIn(signInData: SignInData) {
        viewModelScope.launch {
            val signIn = loginFacade.signInUser(signInData)
            signInState.postValue(signIn)
        }
    }

    fun forgottenPassword(email: String) {
        viewModelScope.launch {
            newPasswordSentState.postValue(loginFacade.sendNewPasswordEmail(email))
        }
    }
}