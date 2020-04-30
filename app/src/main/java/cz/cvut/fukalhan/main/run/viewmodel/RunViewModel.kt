package cz.cvut.fukalhan.main.run.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RunViewModel : ViewModel(), KoinComponent {
    val runRecordState: MutableLiveData<RunRecordSaveState> by lazy { MutableLiveData<RunRecordSaveState>() }
    private val userActivityFacade by inject<UserActivityFacade>()

    fun saveRunRecord(userID: String, date: Long, distance: Double, time: Long, tempo: Long) {
        val record = RunRecord(date = date, distance = distance, time = time, tempo = tempo)
        viewModelScope.launch {
            val recordState = userActivityFacade.saveRunRecord(userID, record)
            runRecordState.postValue(recordState)
        }
    }
}