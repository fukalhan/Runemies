package cz.cvut.fukalhan.main.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.repository.useractivity.UserActivityFacade
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.Date

class ProfileViewModel : ViewModel(), KoinComponent {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val userFacade by inject<UserFacade>()
    private val userActivityFacade by inject<UserActivityFacade>()
    val userData: MutableLiveData<DataWrapper<User>> by lazy { MutableLiveData<DataWrapper<User>>() }
    val monthOverview: MutableLiveData<MutableMap<Float, Double>> by lazy { MutableLiveData<MutableMap<Float, Double>>() }

    fun getUserData(userId: String) {
        viewModelScope.launch {
            userData.postValue(userFacade.getUser(userId))
        }
    }

    fun getUserActivities() {
        user?.let {
            viewModelScope.launch {
                val records = userActivityFacade.getUserActivities(it.uid)
                val monthMileage: MutableMap<Float, Double> = mutableMapOf()
                val formatter = SimpleDateFormat("MMyyyy")
                if (!records.error) {
                    for (record in records.data!!) {
                        val date = formatter.format(Date(record.date)).toFloat()
                        if (monthMileage.containsKey(date)) {
                            monthMileage[date] = monthMileage.getValue(date) + record.distance
                        } else {
                            monthMileage[date] = record.distance
                        }
                    }
                }
                monthOverview.postValue(monthMileage)
            }
        }
    }
}