package cz.cvut.fukalhan.main.run.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.repository.challenges.state.ChallengeState
import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.Route
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordSaveState
import cz.cvut.fukalhan.repository.runrecords.RunRecordsFacade
import cz.cvut.fukalhan.service.LocationTrackingRecord
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RunViewModel : ViewModel(), KoinComponent {
    private val context: Context by inject()
    private var startTime: Long = 0
    private var timeDifference: Long = 0
    private var runRecord = RunRecord()
    private val locationUpdate = LocationUpdate(context).also { it.startLocationUpdates() }
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    private val locationUpdateObserver: Observer<LatLng> = Observer { location.postValue(it) }
    private val locationRecordObserver: Observer<Route> = Observer { updateRecord(it) }
    val location: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>().also {
        locationUpdate.location.observeForever(locationUpdateObserver)
    } }
    val record: MutableLiveData<RunRecord> by lazy { MutableLiveData<RunRecord>() }
    private val runRecordsFacade by inject<RunRecordsFacade>()
    val recordSaveResult: MutableLiveData<RecordSaveState> by lazy { MutableLiveData<RecordSaveState>() }
    private val challengesFacade by inject<ChallengesFacade>()

    fun startRecord() {
        startTime = System.currentTimeMillis()
        runRecord = RunRecord(date = startTime)
        locationUpdate.location.removeObserver(locationUpdateObserver)
        locationTrackingRecord.record.observeForever(locationRecordObserver)
    }

    /** When recording update location and run record */
    private fun updateRecord(route: Route) {
        location.postValue(route.newLocation)
        runRecord.distance += route.distance * 0.001
        runRecord.pace =
            if (runRecord.distance != 0.0) {
                ((System.currentTimeMillis() - startTime) / runRecord.distance).toLong()
            } else {
                0
            }
        runRecord.pathWay += route.newLocation
        record.postValue(runRecord)
    }

    fun pauseRecord() {
        timeDifference = System.currentTimeMillis()
        locationTrackingRecord.record.removeObserver(locationRecordObserver)
        locationUpdate.location.observeForever(locationUpdateObserver)
    }

    fun continueRecord() {
        startTime += System.currentTimeMillis() - timeDifference
        timeDifference = 0
        locationUpdate.location.removeObserver(locationUpdateObserver)
        locationTrackingRecord.record.observeForever(locationRecordObserver)
    }

    fun stopRecord() {
        if (timeDifference != 0.toLong()) {
            timeDifference = System.currentTimeMillis() - timeDifference
        }
        locationUpdate.location.observeForever(locationUpdateObserver)
        locationTrackingRecord.record.removeObserver(locationRecordObserver)
        runRecord.time = System.currentTimeMillis() - timeDifference - startTime
    }

    fun saveRecord(userId: String) {
        viewModelScope.launch {
            val recordSaveState = runRecordsFacade.saveRunRecord(userId, runRecord)
            recordSaveResult.postValue(recordSaveState)
        }
    }

    fun createChallenge(userId: String, username: String, opponentId: String, opponentUsername: String) {
        viewModelScope.launch {
            val challenge = Challenge(
                challengerId = userId,
                challengerUsername = username,
                challengerDistance = runRecord.distance,
                opponentId = opponentId,
                opponentUsername = opponentUsername,
                startDate = startTime,
                state = ChallengeState.STARTED
            )
            challengesFacade.createChallenge(challenge)
        }
    }

    fun updateChallenge(userId: String, challengeId: String) {
        viewModelScope.launch {
            challengesFacade.updateChallenge(challengeId, userId, runRecord.distance)
        }
    }

    override fun onCleared() {
        locationUpdate.stopLocationUpdates()
        locationTrackingRecord.record.removeObserver(locationRecordObserver)
        locationUpdate.location.removeObserver(locationUpdateObserver)
        super.onCleared()
    }
}