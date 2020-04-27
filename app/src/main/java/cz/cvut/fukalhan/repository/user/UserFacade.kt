package cz.cvut.fukalhan.repository.user

import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.DataWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserFacade : IUserFacade, KoinComponent {
    val repository by inject<UserRepository>()
    val repositoryMock by inject<UserRepositoryMock>()

    override suspend fun getUsers(): DataWrapper<List<User>> {
        return repository.getUsers()
    }

    override suspend fun getUser(id: String): User? {
        return repository.getUser(id)
    }
}