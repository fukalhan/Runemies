package cz.cvut.fukalhan.repository.entity

import cz.cvut.fukalhan.repository.challenges.ChallengeType
import java.util.Date

data class Challenge(
    var opponent: User,
    var startDate: Date,
    var endDate: Date,
    var type: ChallengeType
)