package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.states.SignInState
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.repository.entity.User

interface ILoginRepository {
    suspend fun createUser(user: User): SignUpState

    suspend fun registerUser(user: User): SignUpState

    suspend fun signInUser(user: User): SignInState

}