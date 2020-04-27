package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.entity.UserLogin
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginFacade : ILoginFacade, KoinComponent {
    private val repository by inject<LoginRepository>()

    /*override suspend fun createUser(user: User): SignUpState {
        return repository.createUser(user)
    }*/

    override suspend fun registerUser(userLogin: UserLogin): SignUpState {
        return repository.registerUser(userLogin)
    }

    override suspend fun signInUser(userLogin: UserLogin): SignInState {
        return repository.signInUser(userLogin)
    }

    override suspend fun signOutUser(): SignOutState {
        return repository.signOutUser()
    }
}