package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.login.states.SignInState
import cz.cvut.fukalhan.repository.entity.UserLogin
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.login.states.SignUpState

interface ILoginRepository {
    // suspend fun createUser(user: User): SignUpState

    suspend fun registerUser(userLogin: UserLogin): SignUpState

    suspend fun signInUser(userLogin: UserLogin): SignInState

    suspend fun signOutUser(): SignOutState
}