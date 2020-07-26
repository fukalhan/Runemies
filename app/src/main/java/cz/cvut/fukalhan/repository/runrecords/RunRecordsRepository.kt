package cz.cvut.fukalhan.repository.runrecords

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordDeleteState
import cz.cvut.fukalhan.repository.runrecords.states.RecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RunRecordsRepository : IRunRecordsRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Save run record to database under given user ID
     * If run record is saved, add the record to user's statistics
     */
    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RecordSaveState {
        return try {
            val id = db.collection(Constants.RUN_RECORDS).document(userID).collection(Constants.USER_RECORDS)
                .document().id
            runRecord.id = id
            db.collection(Constants.RUN_RECORDS).document(userID).collection(Constants.USER_RECORDS)
                .document(id).set(runRecord).await()
            RecordSaveState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            RecordSaveState.FAIL
        }
    }

    override suspend fun getUserActivities(uid: String): DataWrapper<ArrayList<RunRecord>> {
        val runRecords = ArrayList<RunRecord>()
        return try {
            val snapshot = db.collection(Constants.RUN_RECORDS).document(uid)
                .collection(Constants.USER_RECORDS).get().await()
            snapshot.forEach { doc ->
                runRecords.add(RunRecord(
                    id = doc.data["id"] as String,
                    date = doc.data["date"] as Long,
                    distance = doc.data["distance"] as Double,
                    time = doc.data["time"] as Long,
                    pace = doc.data["pace"] as Long,
                    pathWay = convertToObjectList(doc.data["pathWay"] as ArrayList<Map<String, Double>>)
                ))
            }
            DataWrapper(runRecords)
        } catch (e: Exception) {
            e.printStackTrace()
            DataWrapper(runRecords, true, e.message, e)
        }
    }

    override suspend fun deleteRecord(userId: String, recordId: String): RecordDeleteState {
        return try {
            db.collection(Constants.RUN_RECORDS).document(userId).collection(Constants.USER_RECORDS).document(recordId).delete().await()
            RecordDeleteState.SUCCESS
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            RecordDeleteState.FAIL
        }
    }

    /** Converts Firestore way of storing list of LatLng to actual List<LatLng> */
    private fun convertToObjectList(objects: ArrayList<Map<String, Double>>): List<LatLng> {
        val pathWay = ArrayList<LatLng>()
        objects.forEach { map ->
            val latitude = map["latitude"]
            val longitude = map["longitude"]
            pathWay.add(LatLng(latitude!!, longitude!!))
        }
        return pathWay.toList()
    }
}