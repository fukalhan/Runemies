package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User

class UserRepositoryMock: IUserRepository {

    val users = UsersMock.users

    override suspend fun getUsers(): List<User> {
        return users
    }

    override suspend fun getUser(id: String): User? {
        users.forEach{
         if (it.id == id) { return it }
        }
        return User(username = "Nobody")
    }
}