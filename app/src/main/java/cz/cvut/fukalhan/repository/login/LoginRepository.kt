package cz.cvut.fukalhan.repository.login

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import java.lang.Exception

class LoginRepository: ILoginRepository {

    val db = FirebaseFirestore.getInstance()

    override suspend fun createUser(user: User): SignUpState {
        return try {
            db.collection(Constants.USER).add(user)
            SignUpState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            SignUpState.FAIL
        }
    }
}