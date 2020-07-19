package cz.cvut.fukalhan.main.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.entity.ActivityStatistics
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileViewModel : ViewModel(), KoinComponent {
    val userData: MutableLiveData<DataWrapper<User>> by lazy { MutableLiveData<DataWrapper<User>>() }
    private val userFacade by inject<UserFacade>()
    val userStatistics: MutableLiveData<ActivityStatistics> by lazy { MutableLiveData<ActivityStatistics>() }
    private val userActivityFacade by inject<UserActivityFacade>()

    fun getUserData(userId: String) {
        viewModelScope.launch {
            userData.postValue(userFacade.getUser(userId))
        }
    }

    fun getUserRunStatistics(userId: String) {
        viewModelScope.launch {
            val userActivity = userActivityFacade.getUserActivities(userId)
            var totalMileage = 0.0
            var totalTime: Long = 0
            var longestRun = 0.0
            userActivity.data?.let {
                for (record in it) {
                    totalMileage += record.distance
                    totalTime += record.time
                    if (record.distance > longestRun) {
                        longestRun = record.distance
                    }
                }
            }
            userStatistics.postValue(ActivityStatistics(totalMileage, totalTime, longestRun))
        }
    }
}