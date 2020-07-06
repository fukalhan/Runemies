package cz.cvut.fukalhan.main.run.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RecordSaveState
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.roundToInt

class RunViewModel : ViewModel(), KoinComponent {
    val location: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>() }
    val runRecord: MutableLiveData<RunRecord> by lazy { MutableLiveData<RunRecord>() }
    private var startTime: Long = 0
    private var distance: Double = 0.0
    private var pathWay: List<LatLng> = emptyList()
    private var pace: Long = 0
    private val userActivityFacade by inject<UserActivityFacade>()
    val recordSaveResult: MutableLiveData<RecordSaveState> by lazy { MutableLiveData<RecordSaveState>() }

    fun registerBus() {
        EventBus.getDefault().register(this)
    }

    fun unregisterBus() {
        EventBus.getDefault().unregister(this)
    }

    fun startRecord() {
        startTime = System.currentTimeMillis()
        distance = 0.0
        pathWay = emptyList()
        pace = 0
    }

    @Subscribe
    fun onLocationChanged(newLocation: Location) {
        runRecord.postValue(updateRecord(newLocation))
    }

    private fun updateRecord(location: Location): RunRecord {
        val newLocation = LatLng(location.latitude, location.longitude)
        if (pathWay.isNotEmpty()) {
            val previousLocation = pathWay.last()
            val result = FloatArray(1)
            Location.distanceBetween(
                previousLocation.latitude,
                previousLocation.longitude,
                newLocation.latitude,
                newLocation.longitude,
                result
            )
            distance += (result[0] * 0.1).roundToInt() * 0.01
        }
        pathWay = pathWay + newLocation

        pace = if (distance != 0.0) {
            ((System.currentTimeMillis() - startTime) / distance).toLong()
        } else {
            System.currentTimeMillis() - startTime
        }
        return RunRecord(distance = distance, pathWay = pathWay, pace = pace)
    }

    fun saveRecord(userId: String) {
        val runRecord = RunRecord(startTime, distance, System.currentTimeMillis() - startTime, pace, pathWay)
        viewModelScope.launch {
            recordSaveResult.postValue(userActivityFacade.saveRunRecord(userId, runRecord))
        }
    }
}