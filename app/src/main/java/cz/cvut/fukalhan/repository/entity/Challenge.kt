package cz.cvut.fukalhan.repository.entity

import java.util.*

data class Challenge (
    var oponent: UserProfile,
    var startDate: Date,
    var endDate: Date,
    var type: ChallengeType
    )