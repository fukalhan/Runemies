package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.DataWrapper

class UserRepositoryMock : IUserRepository {

    private val users = UsersMock.users

    override suspend fun getUsers(exceptUser: String?): DataWrapper<List<User>> {
        if (exceptUser != null) {
            val filteredUsers = ArrayList<User>()
            users.forEach { user ->
                if (user.id != exceptUser) {
                    filteredUsers.add(user)
                }
            }
            return DataWrapper(filteredUsers.toList())
        }
        return DataWrapper(users)
    }

    override suspend fun getUser(id: String): User? {
        users.forEach {
            if (it.id == id) {
                return it
            }
        }
        return User(username = "Nobody")
    }
}