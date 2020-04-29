package cz.cvut.fukalhan.repository.useractivity

import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserActivityRepository : IUserActivityRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>> {
        val records = ArrayList<RunRecord>()
        return try {
            val snapshot = db.collection(Constants.RUN_RECORDS).document(uid).collection(Constants.USER_RECORDS).get().await()
            snapshot.forEach { doc ->
                val record = doc.toObject(RunRecord::class.java)
                records.add(record)
            }
            DataWrapper(records)
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(records, true, e.message, e)
        }
    }

    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RunRecordSaveState {
        return try {
            db.collection(Constants.RUN_RECORDS).document(userID).collection(Constants.USER_RECORDS).add(runRecord).await()
            val snapshot = db.collection(Constants.USERS).document(userID).get().await()
            if (snapshot.exists()) {
                val userDoc = snapshot.toObject(User::class.java)
                userDoc?.let { user ->
                    db.collection(Constants.USERS).document(userID).set(updateUserStatistics(user, runRecord)).await()
                }
            }
            RunRecordSaveState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            RunRecordSaveState.FAIL
        }
    }

    private fun updateUserStatistics(user: User, runRecord: RunRecord): User {
        user.totalMileage += runRecord.distance
        user.totalTime += runRecord.time
        if (user.longestRun < runRecord.distance) {
            user.longestRun = runRecord.distance
        }
        if (user.fastest1km > runRecord.fastestKmTime) {
            user.fastest1km = runRecord.fastestKmTime
        }
        if (user.lives < 3) {
            user.mileageToGetLife += runRecord.distance
            while (user.lives < 3 && user.mileageToGetLife >= 10) {
                user.lives++
                user.mileageToGetLife -= 10
            }
        }
        return user
    }
}