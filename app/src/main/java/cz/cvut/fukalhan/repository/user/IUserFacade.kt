package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User

interface IUserFacade {
    suspend fun getUsers(): List<User>

    suspend fun getUser(id: String): User?
}