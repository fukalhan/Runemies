package cz.cvut.fukalhan.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.SignInState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.login.LoginFacade
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {
    val signInState : MutableLiveData<SignInState> by lazy { MutableLiveData<SignInState>() }
    val loginFacade = LoginFacade()


    fun signIn (email: String, password: String) {
        val user = User(email, password)
        viewModelScope.launch {
            //val state = loginFacade.createUser(user)
            val regState = loginFacade.signInUser(user)
            signInState.postValue(regState)
            Log.e(regState.toString(), regState.toString())
        }
    }
}