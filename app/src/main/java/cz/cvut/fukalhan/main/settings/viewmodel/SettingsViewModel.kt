package cz.cvut.fukalhan.main.settings.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.user.UserFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val userFacade by inject<UserFacade>()

    fun setProfileImage(user: FirebaseUser, imageUri: Uri) {
        viewModelScope.launch {
            userFacade.setProfileImage(user, imageUri)
        }
    }

    fun setUsername(user: FirebaseUser, newUsername: String) {
        viewModelScope.launch {
            userFacade.setUsername(user, newUsername)
        }
    }
}