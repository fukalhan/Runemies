package cz.cvut.fukalhan.main.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import cz.cvut.fukalhan.repository.entity.ActivityStatistics
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.repository.runrecords.RunRecordsFacade
import cz.cvut.fukalhan.repository.user.states.ImageSet
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat

class ProfileViewModel : ViewModel(), KoinComponent {
    val userData: MutableLiveData<DataWrapper<User>> by lazy { MutableLiveData<DataWrapper<User>>() }
    private val userFacade by inject<UserFacade>()
    val userStatistics: MutableLiveData<ActivityStatistics> by lazy { MutableLiveData<ActivityStatistics>() }
    private val userActivityFacade by inject<RunRecordsFacade>()
    val monthMileage: MutableLiveData<Array<Float>> by lazy { MutableLiveData<Array<Float>>() }
    val signOutState: MutableLiveData<SignOutState> by lazy { MutableLiveData<SignOutState>() }
    private val loginFacade by inject<LoginFacade>()
    val imageSetState: MutableLiveData<ImageSet> by lazy { MutableLiveData<ImageSet>() }

    fun signOutUser() {
        viewModelScope.launch {
            val signOut = loginFacade.signOutUser()
            signOutState.postValue(signOut)
        }
    }

    fun getUserData(userId: String) {
        viewModelScope.launch {
            userData.postValue(userFacade.getUser(userId))
        }
    }

    fun setProfileImage(uri: Uri, storageRef: StorageReference) {
        viewModelScope.launch {
            imageSetState.postValue(userFacade.setProfileImage(uri, storageRef))
        }
    }

    fun getUserStatistics(userId: String) {
        viewModelScope.launch {
            val userActivity = userActivityFacade.getUserActivities(userId)
            userActivity.data?.let {
                getRunStatistics(it)
                getMonthMileage(it)
            }
        }
    }

    private fun getRunStatistics(userActivities: List<RunRecord>) {
        var totalMileage = 0.0
        var totalTime: Long = 0
        var longestRun = 0.0
        for (record in userActivities) {
            totalMileage += record.distance
            totalTime += record.time
            if (record.distance > longestRun) {
                longestRun = record.distance
            }
        }
        userStatistics.postValue(ActivityStatistics(totalMileage, totalTime, longestRun))
    }

    private fun getMonthMileage(userActivities: List<RunRecord>) {
        val monthDistance: Array<Float> = Array(12) { 0f }
        val timeToDate = SimpleDateFormat("MM")
        for (record in userActivities) {
            val key = timeToDate.format(record.date).toInt()
            monthDistance[key - 1] += record.distance.toFloat()
        }
        monthMileage.postValue(monthDistance)
    }
}