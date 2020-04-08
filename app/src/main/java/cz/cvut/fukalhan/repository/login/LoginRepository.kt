package cz.cvut.fukalhan.repository.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.repository.entity.SignInState
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginRepository: ILoginRepository {

    val db = FirebaseFirestore.getInstance()

    override suspend fun createUser(user: User): SignUpState {
        return try {
            user.id = db.collection(Constants.USER).document().id
            db.collection(Constants.USER).document(user.id).set(user).await()
            SignUpState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            SignUpState.FAIL
        }
    }

    override suspend fun registerUser(user: User): SignUpState {
        return try{
            Log.d("inf", "$user")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email.trim(), user.password).await()
            SignUpState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return when (e) {
                is FirebaseAuthWeakPasswordException ->  SignUpState.WEAK_PASSWORD
                else -> SignUpState.FAIL
            }
        }
    }

    override suspend fun signInUser(user: User): SignInState {
        return try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password).await()
            SignInState.SUCCES
        } catch (e: Exception) {
            e.printStackTrace()
            SignInState.FAIL
        }
    }
}