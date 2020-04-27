package cz.cvut.fukalhan.repository.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRepository: IUserRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUsers(): List<User> {
        val users: ArrayList<User> = ArrayList()
        return try {
            val snapshot = db.collection(Constants.USERS).get().await()
            snapshot.documents.forEach { doc ->
                    val user = doc.toObject(User::class.java)
                    if (user != null){
                    users.add(user)
                }
            }
            users
        } catch (e: Exception) {
            e.printStackTrace()
            users
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
}