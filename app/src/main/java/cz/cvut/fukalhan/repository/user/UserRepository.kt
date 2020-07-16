package cz.cvut.fukalhan.repository.user

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRepository : IUserRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Get all users from database except the exceptUser if specified
     * @return DataWrapper<List<User>> which specifies if some or none users were found or
     * if getting them thrown exception
     */
    override suspend fun getUsers(): DataWrapper<ArrayList<User>> {
        val users: ArrayList<User> = ArrayList()
        return try {
            // Get all records in users collection
            val snapshot = db.collection(Constants.USERS).get().await()
            snapshot.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)
                user?.let { users.add(it) }
            }
            DataWrapper(users)
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(users, true, e.message, e)
        }
    }

    override suspend fun getUser(id: String): User? {
        return try {
            val snapshot = db.collection(Constants.USERS).document(id).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun setProfileImage(user: FirebaseUser, imageUri: Uri) {
        try {
            user.updateProfile(UserProfileChangeRequest.Builder().setPhotoUri(imageUri).build())
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(e.toString(), e.message)
        }
    }

    override suspend fun setUsername(user: FirebaseUser, newUsername: String) {
        try {
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(newUsername).build())
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(e.toString(), e.message)
        }
    }
}