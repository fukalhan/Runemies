package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RecordSaveState
import cz.cvut.fukalhan.shared.DataWrapper

interface IUserActivityFacade {
    suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RecordSaveState
    suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>>
}