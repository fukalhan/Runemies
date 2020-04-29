package cz.cvut.fukalhan.repository.user

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
    override suspend fun getUsers(exceptUser: String?): DataWrapper<List<User>> {
        val users: ArrayList<User> = ArrayList()
        return try {
            // Get all records in users collection
            val snapshot = db.collection(Constants.USERS).get().await()
            // If exceptUser is not specified, get all users
            if (exceptUser == null) {
                snapshot.documents.forEach { doc ->
                    val user = doc.toObject(User::class.java)
                    user?.let { users.add(it) }
                }
            } else {
                snapshot.documents.forEach { doc ->
                    val user = doc.toObject(User::class.java)
                    user?.let {
                        if (user.id != exceptUser) {
                            users.add(it)
                        }
                    }
                }
            }
            DataWrapper(users.toList())
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(users.toList(), true, e.message, e)
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