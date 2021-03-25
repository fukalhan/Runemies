package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState

interface ILoginRepository {
    suspend fun signUpUser(username: String, email: String, password: String): SignUpState
    suspend fun signInUser(email: String, password: String): SignInState
    suspend fun sendNewPasswordEmail(email: String): NewPasswordSentState
    suspend fun signOutUser(): SignOutState
}