package cz.cvut.fukalhan.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    val signUpState : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun signUp (username: String, email: String, password: String) {
        val user = User(email, password, username)
        viewModelScope.launch {
            delay(3000)
            signUpState.postValue(true)
        }
    }
}