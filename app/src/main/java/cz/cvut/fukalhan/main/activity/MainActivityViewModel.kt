package cz.cvut.fukalhan.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.entity.states.SignOutState
import cz.cvut.fukalhan.repository.login.LoginFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel: ViewModel(), KoinComponent {
    val user: MutableLiveData<FirebaseUser?> by lazy { MutableLiveData<FirebaseUser?>()}
    val signOutState: MutableLiveData<SignOutState> by lazy { MutableLiveData<SignOutState>()}
    private val loginFacade by inject<LoginFacade>()

    fun getUser() {
        viewModelScope.launch {
            val firebaseUser = loginFacade.getUser()
            user.postValue(firebaseUser)
        }
    }

    fun signOutUser() {
        viewModelScope.launch {
            val signOut = loginFacade.signOutUser()
            signOutState.postValue(signOut)
        }
    }
}