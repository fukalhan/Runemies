package cz.cvut.fukalhan.repository.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState
import cz.cvut.fukalhan.shared.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginRepository : ILoginRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private suspend fun createUser(user: User): SignUpState {
        return try {
            db.collection(Constants.USERS).document(user.id).set(user).await()
            SignUpState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            SignUpState.FAIL
        }
    }

    override suspend fun signUpUser(username: String, email: String, password: String): SignUpState {
        return try {
            val credentials = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password).await()
            credentials.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
            // Create user with given user auth id and add it to database
            val user = User(
                id = credentials.user?.uid.toString(),
                email = email.trim(),
                username = username,
                joinDate = credentials.user?.metadata?.creationTimestamp ?: 0)
            createUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
            return when (e) {
                is FirebaseAuthWeakPasswordException -> SignUpState.WEAK_PASSWORD
                is FirebaseAuthUserCollisionException -> SignUpState.EMAIL_ALREADY_REGISTERED
                is FirebaseAuthInvalidCredentialsException -> SignUpState.INVALID_EMAIL
                else -> SignUpState.FAIL
            }
        }
    }

    override suspend fun signInUser(email: String, password: String): SignInState {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            SignInState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return when (e) {
                is FirebaseAuthInvalidUserException -> SignInState.NOT_EXISTING_ACCOUNT
                is FirebaseAuthInvalidCredentialsException -> SignInState.WRONG_PASSWORD
                else -> SignInState.FAIL
            }
        }
    }

    override suspend fun signOutUser(): SignOutState {
        return try {
            auth.signOut()
            SignOutState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            SignOutState.FAIL
        }
    }
}