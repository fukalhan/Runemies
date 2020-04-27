package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord

interface IUserActivityRepository {
    suspend fun getUserActivities(uid: String): List<RunRecord>

}