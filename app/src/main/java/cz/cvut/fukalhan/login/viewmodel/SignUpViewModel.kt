package cz.cvut.fukalhan.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.login.LoginFacade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignUpViewModel: ViewModel(), KoinComponent {
    val signUpState : MutableLiveData<SignUpState> by lazy { MutableLiveData<SignUpState>() }
    private val loginFacade by inject<LoginFacade>()

    fun signUp (username: String, email: String, password: String) {
        val user = User(email, password, username)
        viewModelScope.launch {
            //val state = loginFacade.createUser(user)
            val regState = loginFacade.registerUser(user)
            signUpState.postValue(regState)
            Log.e(regState.toString(), regState.toString())
        }
    }
}