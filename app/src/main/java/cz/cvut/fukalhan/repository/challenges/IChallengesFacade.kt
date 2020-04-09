package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge

interface IChallengesFacade {
    suspend fun getChallenges(): List<Challenge>
}