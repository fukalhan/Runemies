package cz.cvut.fukalhan.main.useractivity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.repository.useractivity.states.RecordDeleteState
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityViewModel : ViewModel(), KoinComponent {
    private val user = FirebaseAuth.getInstance().currentUser
    private val userActivityFacade by inject<UserActivityFacade>()
    val activities: MutableLiveData<DataWrapper<ArrayList<RunRecord>>> by lazy { MutableLiveData<DataWrapper<ArrayList<RunRecord>>>() }
    val recordDeleteState: MutableLiveData<RecordDeleteState> by lazy { MutableLiveData<RecordDeleteState>() }

    fun getRecords() {
        user?.let {
            viewModelScope.launch {
                val records = userActivityFacade.getUserActivities(it.uid)
                records.data?.sortWith(RecordsComparator())
                activities.postValue(records)
            }
        }
    }

    fun deleteRecord(recordId: String) {
        user?.let {
            viewModelScope.launch {
                recordDeleteState.postValue(userActivityFacade.deleteRecord(it.uid, recordId))
            }
        }
    }
}