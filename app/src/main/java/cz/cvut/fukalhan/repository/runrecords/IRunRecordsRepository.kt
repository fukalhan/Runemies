package cz.cvut.fukalhan.repository.runrecords

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordDeleteState
import cz.cvut.fukalhan.repository.runrecords.states.RecordSaveState
import cz.cvut.fukalhan.shared.DataWrapper

interface IRunRecordsRepository {
    suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RecordSaveState
    suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>>
    suspend fun deleteRecord(userId: String, recordId: String): RecordDeleteState
}