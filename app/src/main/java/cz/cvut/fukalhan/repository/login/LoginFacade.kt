package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.login.signin.SignInData
import cz.cvut.fukalhan.login.signup.SignUpData
import cz.cvut.fukalhan.repository.login.state.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.state.SignInState
import cz.cvut.fukalhan.repository.login.state.SignOutState
import cz.cvut.fukalhan.repository.login.state.SignUpState

interface LoginFacade {
    suspend fun signUpUser(signUpData: SignUpData): SignUpState
    suspend fun signInUser(signInData: SignInData): SignInState
    suspend fun sendNewPasswordEmail(email: String): NewPasswordSentState
    suspend fun signOutUser(): SignOutState
}