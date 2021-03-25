package cz.cvut.fukalhan.main.enemies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemiesViewModel : ViewModel(), KoinComponent {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val userFacade by inject<UserFacade>()
    val enemies: MutableLiveData<DataWrapper<ArrayList<User>>> by lazy { MutableLiveData<DataWrapper<ArrayList<User>>>() }

    fun getEnemies() {
        viewModelScope.launch {
            user?.let {
                enemies.postValue(userFacade.getEnemies(it.uid))
            }
        }
    }
}