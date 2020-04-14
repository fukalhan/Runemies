package cz.cvut.fukalhan.repository.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.entity.states.SignInState
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.entity.UserLogin
import cz.cvut.fukalhan.shared.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginRepository: ILoginRepository {

    val db = FirebaseFirestore.getInstance()

    override suspend fun createUser(user: User): SignUpState {
        return try {
            db.collection(Constants.USERS).document(user.id).set(user).await()
            SignUpState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            SignUpState.FAIL
        }
    }

    override suspend fun registerUser(userLogin: UserLogin): SignUpState {
        return try{
            val credentials = FirebaseAuth.getInstance().createUserWithEmailAndPassword(userLogin.email.trim(), userLogin.password).await()
            // Create user with given user auth id and set it to database
            val user = User(userLogin.email.trim(), userLogin.username, credentials.user?.uid.toString())
            createUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
            return when (e) {
                is FirebaseAuthWeakPasswordException ->  SignUpState.WEAK_PASSWORD
                else -> SignUpState.FAIL
            }
        }
    }

    override suspend fun signInUser(userLogin: UserLogin): SignInState {
        return try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userLogin.email, userLogin.password).await()
            SignInState.SUCCES
        } catch (e: Exception) {
            e.printStackTrace()
            SignInState.FAIL
        }
    }
}