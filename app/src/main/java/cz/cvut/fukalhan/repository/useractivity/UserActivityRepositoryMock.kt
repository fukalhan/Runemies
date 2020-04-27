package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord

class UserActivityRepositoryMock: IUserActivityRepository {
    override suspend fun getUserActivities(uid: String): List<RunRecord> {
        return UserActivitiesMock.runRecords
    }
}