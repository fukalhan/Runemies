package cz.cvut.fukalhan.repository.user

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import cz.cvut.fukalhan.main.enemies.viewmodel.EnemiesComparator
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.states.ImageSet
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRepository : IUserRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUser(userId: String): DataWrapper<User> {
        return try {
            val snapshot = db.collection(Constants.USERS).document(userId).get().await()
            if (snapshot.exists()) {
                DataWrapper(snapshot.toObject(User::class.java), false)
            } else {
                DataWrapper(null, false, "User doesn't exists")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(null, true, e.message)
        }
    }

    override suspend fun getUsers(): DataWrapper<ArrayList<User>> {
        val users: ArrayList<User> = ArrayList()
        return try {
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

    override suspend fun getEnemies(uid: String): DataWrapper<ArrayList<User>> {
        val enemies: ArrayList<User> = ArrayList()
        return try {
            val snapshot = db.collection(Constants.USERS).get().await()
            snapshot.documents.forEach { doc ->
                val user = doc.toObject(User::class.java)
                if (user != null && user.id != uid) {
                    enemies.add(user)
                }
            }
            enemies.sortWith(EnemiesComparator())
            DataWrapper(enemies)
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(enemies, true, e.message, e)
        }
    }

    override suspend fun setProfileImage(uri: Uri, storageRef: StorageReference): ImageSet {
        return try {
            storageRef.putFile(uri)
            ImageSet.SUCCESS
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            ImageSet.FAIL
        }
    }
}