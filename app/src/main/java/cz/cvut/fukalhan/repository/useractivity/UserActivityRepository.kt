package cz.cvut.fukalhan.repository.useractivity

import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.entity.RunRecord
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

    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RunRecordState {
        return try {
            db.collection(Constants.RUN_RECORDS).document(userID).collection(Constants.USER_RECORDS).add(runRecord).await()
            RunRecordState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            RunRecordState.FAIL
        }
    }
}