package cz.cvut.fukalhan.repository.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.DataWrapper

interface IUserRepository {
    suspend fun getUser(userId: String): DataWrapper<User>
    suspend fun getUsers(): DataWrapper<ArrayList<User>>
    suspend fun setProfileImage(user: FirebaseUser, imageUri: Uri)
    suspend fun setUsername(user: FirebaseUser, newUsername: String)
}