package cz.cvut.fukalhan.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.login.LoginFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel: ViewModel(), KoinComponent {
    val user: MutableLiveData<FirebaseUser?> by lazy { MutableLiveData<FirebaseUser?>()}
    private val loginFacade by inject<LoginFacade>()

    fun getUser() {
        viewModelScope.launch {
            val firebaseUser = loginFacade.getUser()
            user.postValue(firebaseUser)
        }
    }
}