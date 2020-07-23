package cz.cvut.fukalhan.repository.entity

import cz.cvut.fukalhan.repository.challenges.state.ChallengeState

data class Challenge(
    var id: String = "",
    var challengerId: String = "",
    var challengerUsername: String = "",
    var challengerDistance: Double = 0.0,
    var opponentId: String = "",
    var opponentUsername: String = "",
    var opponentDistance: Double = 0.0,
    var startDate: Long = 0,
    var state: ChallengeState = ChallengeState.NULL
)