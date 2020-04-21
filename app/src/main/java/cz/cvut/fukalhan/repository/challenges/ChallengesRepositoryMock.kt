package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.states.ChallengeType
import cz.cvut.fukalhan.repository.entity.User
import java.util.*
import kotlin.collections.ArrayList

class ChallengesRepositoryMock: IChallengesRepository {
    override suspend fun getChallenges(): List<Challenge> {
        val challenges = ArrayList<Challenge>()
        challenges.add(Challenge(User(username="fukalhan", email ="fukalhan@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User(username="Ilona", email = "ilonka@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        challenges.add(Challenge(User(username="Dominik", email = "dominik@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User(username="Narek", email = "Narek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User(username="Vašek", email = "vasek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        challenges.add(Challenge(User(username="Štěpanisko", email = "sterba@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User(username="Čonkos", email = "cokos@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User(username="Čenda", email = "matka.priroda@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User(username="Chlupata čubka", email = "apakrychle@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        return challenges
    }
}