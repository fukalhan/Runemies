package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.states.SignInState
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.entity.UserLogin

interface ILoginFacade {
    suspend fun createUser(user: User): SignUpState

    suspend fun registerUser(userLogin: UserLogin): SignUpState

    suspend fun signInUser(userLogin: UserLogin): SignInState
}