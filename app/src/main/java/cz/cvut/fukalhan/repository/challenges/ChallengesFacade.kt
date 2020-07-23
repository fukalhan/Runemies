package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.DataWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChallengesFacade : IChallengesFacade, KoinComponent {
    private val repository by inject<ChallengesRepository>()

    override suspend fun createChallenge(challenge: Challenge) {
        repository.createChallenge(challenge)
    }

    override suspend fun updateChallenge(challengeId: String, userId: String, userDistance: Double) {
        repository.updateChallenge(challengeId, userId, userDistance)
    }

    override suspend fun getRequestedChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return repository.getRequestedChallenges(userId)
    }

    override suspend fun getActiveChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return repository.getActiveChallenges(userId)
    }

    override suspend fun getCompletedChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return repository.getCompletedChallenges(userId)
    }
}