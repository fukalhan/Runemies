package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.SignInState
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginFacade: ILoginFacade, KoinComponent {
    private val repository by inject<LoginRepository>()

    override suspend fun createUser(user: User): SignUpState {
        return repository.createUser(user)
    }

    override suspend fun registerUser(user: User): SignUpState {
        return repository.registerUser(user)
    }

    override suspend fun signInUser(user: User): SignInState {
        return repository.signInUser(user)
    }
}