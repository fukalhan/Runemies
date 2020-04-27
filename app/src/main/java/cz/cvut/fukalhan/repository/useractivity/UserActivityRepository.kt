package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord

class UserActivityRepository: IUserActivityRepository {

    override suspend fun getUserActivities(uid: String): List<RunRecord> {
        TODO("Not yet implemented")
    }
}