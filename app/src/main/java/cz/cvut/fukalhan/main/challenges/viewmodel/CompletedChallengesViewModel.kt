package cz.cvut.fukalhan.main.challenges.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompletedChallengesViewModel : ViewModel(), KoinComponent {
    private val challengesFacade by inject<ChallengesFacade>()
    val challenges: MutableLiveData<DataWrapper<ArrayList<Challenge>>> by lazy { MutableLiveData<DataWrapper<ArrayList<Challenge>>>() }

    fun getChallenges(userId: String) {
        viewModelScope.launch {
            val completedChallenges = challengesFacade.getCompletedChallenges(userId)
            completedChallenges.data?.sortWith(ChallengesDateComparator())
            challenges.postValue(completedChallenges)
        }
    }
}