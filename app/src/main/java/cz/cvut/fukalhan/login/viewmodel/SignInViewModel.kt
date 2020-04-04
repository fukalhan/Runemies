package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel: ViewModel() {
    val signInState : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun signIn (email: String, password: String) {
        signInState.postValue(true)
    }
}