package cz.cvut.fukalhan.repository.entity

data class RunRecord (
    var id: String = "",
    var date: Long = 0,
    var distance: Double = 0.0,
    var time:  Long = 0,
    var tempo: Long = 0
)