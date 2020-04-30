package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.DataWrapper

interface IUserActivityRepository {
    suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>>

    suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RunRecordSaveState
}