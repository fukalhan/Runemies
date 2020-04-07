package cz.cvut.fukalhan.repository.login

import cz.cvut.fukalhan.repository.entity.SignUpState
import cz.cvut.fukalhan.repository.entity.User

interface ILoginRepository {
    suspend fun createUser(user: User): SignUpState
}