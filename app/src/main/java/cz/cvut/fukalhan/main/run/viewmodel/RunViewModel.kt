package cz.cvut.fukalhan.main.run.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.repository.entity.Route
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RecordSaveState
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.service.LocationTrackingRecord
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RunViewModel(context: Context) : ViewModel(), KoinComponent {
    private var startTime: Long = 0
    private var runRecord = RunRecord()
    private val locationUpdateObserver: Observer<LatLng> = Observer { location.postValue(it) }
    private val locationRecordObserver: Observer<Route> = Observer {
        updateRecord(it)
    }
    val location: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>().also {
        locationUpdate.location.observeForever(locationUpdateObserver)
    } }
    private val locationUpdate = LocationUpdate(context).also { it.startLocationUpdates() }
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    val record: MutableLiveData<RunRecord> by lazy { MutableLiveData<RunRecord>() }
    private val userActivityFacade by inject<UserActivityFacade>()
    val recordSaveResult: MutableLiveData<RecordSaveState> by lazy { MutableLiveData<RecordSaveState>() }

    fun startRecord() {
        startTime = System.currentTimeMillis()
        runRecord = RunRecord(startTime)
        location.removeObserver(locationUpdateObserver)
        locationTrackingRecord.record.observeForever(locationRecordObserver)
    }

    private fun updateRecord(route: Route) {
        location.postValue(route.pathPoints.last())
        runRecord.pace =
            if (route.distance != 0.0) {
                ((System.currentTimeMillis() - startTime) / route.distance).toLong()
            } else {
                0
            }
        runRecord.distance = route.distance
        runRecord.pathWay = route.pathPoints
        record.postValue(runRecord)
    }

    fun stopRecord() {
        locationUpdate.location.observeForever(locationUpdateObserver)
        locationTrackingRecord.record.removeObserver(locationRecordObserver)
    }

    fun saveRecord(userId: String) {
        viewModelScope.launch {
            runRecord.time = System.currentTimeMillis() - startTime
            val recordSaveState = userActivityFacade.saveRunRecord(userId, runRecord)
            recordSaveResult.postValue(recordSaveState)
        }
    }

    override fun onCleared() {
        locationUpdate.stopLocationUpdates()
        locationTrackingRecord.record.removeObserver(locationRecordObserver)
        locationUpdate.location.removeObserver(locationUpdateObserver)
        super.onCleared()
    }
}