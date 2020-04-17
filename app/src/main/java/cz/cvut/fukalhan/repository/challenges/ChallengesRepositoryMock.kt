package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.states.ChallengeType
import cz.cvut.fukalhan.repository.entity.User
import java.util.*
import kotlin.collections.ArrayList

class ChallengesRepositoryMock: IChallengesRepository {
    override suspend fun getChallenges(): List<Challenge> {
        val challenges = ArrayList<Challenge>()
        challenges.add(Challenge(User("fukalhan", "fukalhan@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User("Ilona", "ilonka@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        challenges.add(Challenge(User("Dominik", "dominik@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User("Narek", "Narek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User("Vašek", "vasek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        challenges.add(Challenge(User("Štěpanisko", "sterba@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        challenges.add(Challenge(User("Čonkos", "cokos@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User("Čenda", "matka.priroda@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        challenges.add(Challenge(User("Chlupata čubka", "apakrychle@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        return challenges
    }
}