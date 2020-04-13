package cz.cvut.fukalhan.main.challenges.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActiveChallengesViewModel: ViewModel(), KoinComponent {

    val challenges : MutableLiveData<List<Challenge>> by lazy { MutableLiveData<List<Challenge>>() }
    private val challengeFacade by inject<ChallengesFacade>()

    fun getChallenges() {
        viewModelScope.launch {
            val challengesList = challengeFacade.getChallenges()
            challenges.postValue(challengesList)
        }
    }
}