package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.login.signin.SignInData
import cz.cvut.fukalhan.login.signup.SignUpData
import cz.cvut.fukalhan.repository.login.state.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.state.SignInState
import cz.cvut.fukalhan.repository.login.state.SignOutState
import cz.cvut.fukalhan.repository.login.state.SignUpState

class LoginFacadeImpl(private val repository: LoginRepository) : LoginFacade {

    override suspend fun signUpUser(signUpData: SignUpData): SignUpState {
        return repository.signUpUser(signUpData)
    }

    override suspend fun signInUser(signInData: SignInData): SignInState {
        return repository.signInUser(signInData)
    }

    override suspend fun sendNewPasswordEmail(email: String): NewPasswordSentState {
        return repository.sendNewPasswordEmail(email)
    }

    override suspend fun signOutUser(): SignOutState {
        return repository.signOutUser()
    }
}