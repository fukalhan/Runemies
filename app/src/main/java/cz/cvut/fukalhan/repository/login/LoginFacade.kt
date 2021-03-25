package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginFacade : ILoginFacade, KoinComponent {
    private val repository by inject<LoginRepository>()

    override suspend fun signUpUser(username: String, email: String, password: String): SignUpState {
        return repository.signUpUser(username, email, password)
    }

    override suspend fun signInUser(email: String, password: String): SignInState {
        return repository.signInUser(email, password)
    }

    override suspend fun sendNewPasswordEmail(email: String): NewPasswordSentState {
        return repository.sendNewPasswordEmail(email)
    }

    override suspend fun signOutUser(): SignOutState {
        return repository.signOutUser()
    }
}