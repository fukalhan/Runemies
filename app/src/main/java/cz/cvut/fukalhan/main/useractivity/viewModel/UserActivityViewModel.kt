package cz.cvut.fukalhan.main.useractivity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserActivityViewModel : ViewModel(), KoinComponent {
    val userActivity: MutableLiveData<List<RunRecord>> by lazy { MutableLiveData<List<RunRecord>>() }
    val userActivityFacade by inject<UserActivityFacade>()

    fun getUserActivities(uid: String) {
        viewModelScope.launch {
            val userActivities = userActivityFacade.getUserActivities(uid)
            userActivity.postValue(userActivities)
        }
    }
}