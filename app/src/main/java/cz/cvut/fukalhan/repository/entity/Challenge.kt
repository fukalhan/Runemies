package cz.cvut.fukalhan.repository.entity

import cz.cvut.fukalhan.repository.challenges.ChallengeState

data class Challenge(
    var id: String,
    var user1: User,
    var user2: User,
    var startDate: Long,
    var distance: Double,
    var state: ChallengeState
)