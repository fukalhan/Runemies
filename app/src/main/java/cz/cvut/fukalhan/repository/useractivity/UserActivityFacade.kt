package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.DataWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityFacade : IUserActivityFacade, KoinComponent {
    private val repository by inject<UserActivityRepository>()
    private val repositoryMock by inject<UserActivityRepositoryMock>()

    override suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>> {
        return repository.getUserActivities(uid)
    }

    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RunRecordSaveState {
        return repository.saveRunRecord(userID, runRecord)
    }
}