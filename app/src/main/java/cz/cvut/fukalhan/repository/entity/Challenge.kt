package cz.cvut.fukalhan.repository.entity

import cz.cvut.fukalhan.repository.challenges.ChallengeState

data class Challenge(
    var id: String = "",
    var challengerId: String = "",
    var opponentId: String = "",
    var startDate: Long = 0,
    var distance: Double = 0.0,
    var state: ChallengeState = ChallengeState.ACTIVE
)