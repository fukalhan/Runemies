package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.SignInState
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User

class LoginFacade: ILoginFacade {
    private val repository = LoginRepository()

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