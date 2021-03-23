package cz.cvut.fukalhan.main.challenges.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActiveChallengesViewModel : ViewModel(), KoinComponent {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val challengesFacade by inject<ChallengesFacade>()
    val challenges: MutableLiveData<DataWrapper<ArrayList<Challenge>>> by lazy { MutableLiveData<DataWrapper<ArrayList<Challenge>>>() }

    fun getChallenges() {
        viewModelScope.launch {
            user?.let {
                val activeChallenges = challengesFacade.getActiveChallenges(it.uid)
                activeChallenges.data?.sortWith(ChallengesDateComparator())
                challenges.postValue(activeChallenges)
            }
        }
    }
}