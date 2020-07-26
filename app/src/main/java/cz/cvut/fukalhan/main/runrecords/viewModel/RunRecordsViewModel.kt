package cz.cvut.fukalhan.main.runrecords.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.RunRecordsFacade
import cz.cvut.fukalhan.repository.runrecords.states.RecordDeleteState
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RunRecordsViewModel : ViewModel(), KoinComponent {
    private val user = FirebaseAuth.getInstance().currentUser
    private val runRecordsFacade by inject<RunRecordsFacade>()
    val runRecords: MutableLiveData<DataWrapper<ArrayList<RunRecord>>> by lazy { MutableLiveData<DataWrapper<ArrayList<RunRecord>>>() }
    val recordDeleteState: MutableLiveData<RecordDeleteState> by lazy { MutableLiveData<RecordDeleteState>() }

    fun getRecords() {
        user?.let {
            viewModelScope.launch {
                val records = runRecordsFacade.getUserActivities(it.uid)
                records.data?.sortWith(RunRecordsComparator())
                runRecords.postValue(records)
            }
        }
    }

    fun deleteRecord(recordId: String) {
        user?.let {
            viewModelScope.launch {
                recordDeleteState.postValue(runRecordsFacade.deleteRecord(it.uid, recordId))
            }
        }
    }
}