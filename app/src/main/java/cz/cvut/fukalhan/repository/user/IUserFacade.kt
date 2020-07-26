package cz.cvut.fukalhan.repository.user

import android.net.Uri
import com.google.firebase.storage.StorageReference
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.states.ImageSet
import cz.cvut.fukalhan.shared.DataWrapper

interface IUserFacade {
    suspend fun getUser(userId: String): DataWrapper<User>
    suspend fun getUsers(): DataWrapper<ArrayList<User>>
    suspend fun setProfileImage(uri: Uri, storageRef: StorageReference): ImageSet
}