package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User

class LoginFacade: ILoginFacade {
    private val repository = LoginRepository()

    override suspend fun createUser(user: User): SignUpState {
        return repository.createUser(user)
    }
}