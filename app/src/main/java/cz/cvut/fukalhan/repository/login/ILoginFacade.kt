package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.SignInState
import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User

interface ILoginFacade {
    suspend fun createUser(user: User): SignUpState

    suspend fun registerUser(user: User): SignUpState

    suspend fun signInUser(user: User): SignInState
}