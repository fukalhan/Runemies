package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge

class ChallengesRepository: IChallengesRepository {
    override suspend fun getChallenges(): List<Challenge> {
        TODO("Not yet implemented")
    }
}