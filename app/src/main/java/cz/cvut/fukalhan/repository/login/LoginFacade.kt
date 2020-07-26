package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.PasswordChangeEmailSentState
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

    override suspend fun sendChangePasswordEmail(email: String): PasswordChangeEmailSentState {
        return repository.sendChangePasswordEmail(email)
    }

    override suspend fun signOutUser(): SignOutState {
        return repository.signOutUser()
    }
}