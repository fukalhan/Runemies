package cz.cvut.fukalhan.main.challenges.viewmodel

import cz.cvut.fukalhan.repository.entity.Challenge

class ChallengesDateComparator : Comparator<Challenge> {
    override fun compare(challenge1: Challenge?, challenge2: Challenge?): Int {
        return when {
            challenge1?.startDate == challenge2?.startDate -> 0
            challenge1?.startDate!! < challenge2?.startDate!! -> 1
            else -> -1
        }
    }
}