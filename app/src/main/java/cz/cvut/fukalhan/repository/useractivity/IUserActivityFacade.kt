package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord

interface IUserActivityFacade {
    suspend fun getUserActivities(uid: String): List<RunRecord>
}