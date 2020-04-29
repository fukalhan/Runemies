package cz.cvut.fukalhan.main.useractivity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityViewModel : ViewModel(), KoinComponent {
    val activitiesReceiver: MutableLiveData<DataWrapper<ArrayList<RunRecord>>> by lazy { MutableLiveData<DataWrapper<ArrayList<RunRecord>>>() }
    private val userActivityFacade by inject<UserActivityFacade>()

    fun getUserActivities(uid: String) {
        viewModelScope.launch {
            activitiesReceiver.postValue(userActivityFacade.getUserActivities(uid))
        }
    }
}