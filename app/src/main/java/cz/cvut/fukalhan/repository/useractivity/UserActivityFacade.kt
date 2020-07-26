package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RecordDeleteState
import cz.cvut.fukalhan.repository.useractivity.states.RecordSaveState
import cz.cvut.fukalhan.shared.DataWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityFacade : IUserActivityFacade, KoinComponent {
    private val repository by inject<UserActivityRepository>()

    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RecordSaveState {
        return repository.saveRunRecord(userID, runRecord)
    }

    override suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>> {
        return repository.getUserActivities(uid)
    }

    override suspend fun deleteRecord(userId: String, recordId: String): RecordDeleteState {
        return repository.deleteRecord(userId, recordId)
    }
}