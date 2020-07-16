package cz.cvut.fukalhan.repository.useractivity

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.useractivity.states.RecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserActivityRepository : IUserActivityRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Save run record to database under given user ID
     * If run record is saved, add the record to user's statistics
     */
    override suspend fun saveRunRecord(userID: String, runRecord: RunRecord): RecordSaveState {
        return try {
            when (addRunRecord(userID, runRecord)) {
                RecordSaveState.CANNOT_ADD_RECORD -> RecordSaveState.CANNOT_ADD_RECORD
                else -> updateUser(userID, runRecord)
            }
            RecordSaveState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            RecordSaveState.FAIL
        }
    }

    /** Add run record to the database */
    private suspend fun addRunRecord(userID: String, runRecord: RunRecord): RecordSaveState {
        return try {
            db.collection(Constants.RUN_RECORDS).document(userID).collection(Constants.USER_RECORDS)
                .add(runRecord).await()
            RecordSaveState.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            RecordSaveState.CANNOT_ADD_RECORD
        }
    }

    private suspend fun updateUser(userID: String, runRecord: RunRecord): RecordSaveState {
        return try {
            val snapshot = db.collection(Constants.USERS).document(userID).get().await()
            if (snapshot.exists()) {
                val userDoc = snapshot.toObject(User::class.java)
                userDoc?.let { user ->
                    db.collection(Constants.USERS).document(userID)
                        .set(updateUserStatistics(user, runRecord)).await()
                }
                RecordSaveState.SUCCESS
            } else {
                RecordSaveState.NOT_EXISTING_USER
            }
        } catch (e: Exception) {
            e.printStackTrace()
            RecordSaveState.CANNOT_UPDATE_STATISTICS
        }
    }
    /** Update user statistics according with new run record */
    private fun updateUserStatistics(user: User, runRecord: RunRecord): User {
        user.totalMileage += runRecord.distance
        user.totalTime += runRecord.time
        if (user.longestRun < runRecord.distance) {
            user.longestRun = runRecord.distance
        }
        return updateUserLivesCount(user, runRecord)
    }

    /**
     * When user has less than 3 lives, for every 10 km he runs, he gets 1 life
     * mileageToGetLife is user counter of km he needs to get lives
     * if user doesn't have less than 3 lives, the distance from run record isn't accounted in mileageToGetLife
     * the mileage starts to be counted when user has less than 3 lives
     */
    private fun updateUserLivesCount(user: User, runRecord: RunRecord): User {
        if (user.lives < 3) {
            user.mileageToGetLife += runRecord.distance
        }
        while (user.lives < 3 && user.mileageToGetLife >= 10) {
            user.lives++
            user.mileageToGetLife -= 10
        }
        // If user already has 3 lives, the counter is reset
        if (user.lives == 3) {
            user.mileageToGetLife = 0.0
        }
        return user
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