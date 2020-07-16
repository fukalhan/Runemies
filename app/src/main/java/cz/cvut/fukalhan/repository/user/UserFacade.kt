package cz.cvut.fukalhan.repository.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.DataWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserFacade : IUserFacade, KoinComponent {
    val repository by inject<UserRepository>()

    override suspend fun getUser(userId: String): DataWrapper<User> {
        return repository.getUser(userId)
    }

    override suspend fun getUsers(): DataWrapper<ArrayList<User>> {
        return repository.getUsers()
    }

    override suspend fun setProfileImage(user: FirebaseUser, imageUri: Uri) {
        repository.setProfileImage(user, imageUri)
    }

    override suspend fun setUsername(user: FirebaseUser, newUsername: String) {
        repository.setUsername(user, newUsername)
    }
}