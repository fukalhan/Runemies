package cz.cvut.fukalhan.repository.entity

import java.util.*

data class Challenge (
    var oponent: User,
    var startDate: Date,
    var endDate: Date,
    var type: ChallengeType
    )