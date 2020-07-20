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

class ActiveChallengesViewModel : ViewModel(), KoinComponent {
    val challenges: MutableLiveData<DataWrapper<List<Challenge>>> by lazy { MutableLiveData<DataWrapper<List<Challenge>>>() }
    private val challengesFacade by inject<ChallengesFacade>()

    fun getChallenges(userId: String) {
        viewModelScope.launch {
            val challengesList = challengesFacade.getActiveChallenges(userId)
            challenges.postValue(challengesList)
        }
    }
}