package cz.cvut.fukalhan.main.run.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.shared.LocationTrackingRecord
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RunViewModel(private val lifecycleOwner: LifecycleOwner) : ViewModel(), KoinComponent {
    val runRecordState: MutableLiveData<RunRecordSaveState> by lazy { MutableLiveData<RunRecordSaveState>() }
    private val userActivityFacade by inject<UserActivityFacade>()
    private val locationTrackingResult by inject<LocationTrackingRecord>()

    fun saveRunRecord(userID: String) {
        locationTrackingResult.record.observe(lifecycleOwner, Observer { runRecord ->
            viewModelScope.launch {
                runRecordState.postValue(userActivityFacade.saveRunRecord(userID, runRecord))
            }
        })
    }
}