package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.PasswordChangeEmailSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState

interface ILoginRepository {
    suspend fun signUpUser(username: String, email: String, password: String): SignUpState
    suspend fun signInUser(email: String, password: String): SignInState
    suspend fun sendChangePasswordEmail(email: String): PasswordChangeEmailSentState
    suspend fun signOutUser(): SignOutState
}