package cz.cvut.fukalhan.main.run.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
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
    private val userActivityFacade by inject<UserActivityFacade>()
    val recordSaveState: MutableLiveData<RunRecordSaveState> by lazy { MutableLiveData<RunRecordSaveState>() }

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
    }

    @Subscribe
    fun onLocationChanged(newLocation: Location) {
        // location.postValue(LatLng(newLocation.latitude, newLocation.longitude))
        runRecord.postValue(updateRecords(newLocation))
    }

    private fun updateRecords(location: Location): RunRecord {
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

        val runRecord = RunRecord(distance = distance, pathWay = pathWay)
        if (distance != 0.0) {
            runRecord.pace = ((System.currentTimeMillis() - startTime) / distance).toLong()
        }
        return runRecord
    }
}