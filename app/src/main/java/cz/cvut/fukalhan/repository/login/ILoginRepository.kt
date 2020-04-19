package cz.cvut.fukalhan.repository.login

import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.entity.states.SignInState
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.entity.UserLogin
import cz.cvut.fukalhan.repository.entity.states.SignOutState

interface ILoginRepository {
    //suspend fun createUser(user: User): SignUpState

    suspend fun registerUser(userLogin: UserLogin): SignUpState

    suspend fun signInUser(userLogin: UserLogin): SignInState

    suspend fun signOutUser(): SignOutState

}