package cz.cvut.fukalhan.repository.login

import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.entity.states.SignInState
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.entity.UserLogin
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginFacade: ILoginFacade, KoinComponent {
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

    override suspend fun getUser(): FirebaseUser? {
        return repository.getUser()
    }

}