package cz.cvut.fukalhan.repository.entity

import java.sql.Time
import java.util.*

data class RunRecord (
    var date: Date,
    var length: Double,
    var time: Time,
    var elevation: Double
    )