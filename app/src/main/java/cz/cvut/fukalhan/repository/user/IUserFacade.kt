package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.DataWrapper

interface IUserFacade {
    suspend fun getUsers(): DataWrapper<List<User>>

    suspend fun getUser(id: String): User?
}