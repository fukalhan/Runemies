package cz.cvut.fukalhan.main.challenges.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.challenges.state.ChallengeDeleteState
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RequestedChallengesViewModel : ViewModel(), KoinComponent {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val challengesFacade by inject<ChallengesFacade>()
    val challenges: MutableLiveData<DataWrapper<ArrayList<Challenge>>> by lazy { MutableLiveData<DataWrapper<ArrayList<Challenge>>>() }
    val challengeDeleteState: MutableLiveData<ChallengeDeleteState> by lazy { MutableLiveData<ChallengeDeleteState>() }

    fun getChallenges() {
        viewModelScope.launch {
            user?.let {
                val requestedChallenges = challengesFacade.getRequestedChallenges(it.uid)
                requestedChallenges.data?.sortWith(ChallengesDateComparator())
                challenges.postValue(requestedChallenges)
            }
        }
    }

    fun deleteChallenge(challengeId: String) {
        viewModelScope.launch {
            challengeDeleteState.postValue(challengesFacade.deleteChallenge(challengeId))
        }
    }
}