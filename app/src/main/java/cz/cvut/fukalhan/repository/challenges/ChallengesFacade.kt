package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChallengesFacade: IChallengesFacade, KoinComponent {

    private val repository by inject<ChallengesRepository>()
    private val fakeRepository by inject<FakeChallengesRepository>()

    override suspend fun getChallenges(): List<Challenge> {
        return fakeRepository.getChallenges()
    }

}