package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityFacade: IUserActivityFacade, KoinComponent {
    val repository by inject<UserActivityRepository>()
    val repositoryMock by inject<UserActivityRepositoryMock>()

    override suspend fun getUserActivities(uid: String): List<RunRecord> {
        return repositoryMock.getUserActivities(uid)
    }
}