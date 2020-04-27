package cz.cvut.fukalhan.repository.useractivity

import android.provider.SyncStateContract
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.shared.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserActivityRepository: IUserActivityRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUserActivities(uid: String): List<RunRecord> {
        val records = ArrayList<RunRecord>()
        return try {
            val snapshot = db.collection(Constants.RUN_RECORDS).document(uid).collection(Constants.USER_RECORDS).get().await()
            snapshot.forEach{ doc ->
                val record = doc.toObject(RunRecord::class.java)
                records.add(record)
            }
            records
        } catch (e: Exception) {
            e.printStackTrace()
            records
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