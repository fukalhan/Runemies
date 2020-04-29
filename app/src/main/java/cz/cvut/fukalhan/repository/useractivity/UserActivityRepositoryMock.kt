package cz.cvut.fukalhan.repository.useractivity

import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.shared.DataWrapper

class UserActivityRepositoryMock : IUserActivityRepository {
    override suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>> {
        return DataWrapper(UserActivitiesMock.runRecords)
    }

    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RunRecordSaveState {
        TODO("Not yet implemented")
    }
}