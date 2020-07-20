package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.DataWrapper

interface IChallengesRepository {
    suspend fun createChallenge(challenge: Challenge)
    suspend fun getActiveChallenges(userId: String): DataWrapper<List<Challenge>>
    suspend fun getCompletedChallenges(userId: String): DataWrapper<List<Challenge>>
}