package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.ChallengeType
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.entity.UserProfile
import java.util.*
import kotlin.collections.ArrayList

class FakeChallengesRepository: IChallengesRepository {
    override suspend fun getChallenges(): List<Challenge> {
        val list = ArrayList<Challenge>()
        list.add(Challenge(UserProfile("fukalhan", "fukalhan@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        list.add(Challenge(UserProfile("Ilona", "ilonka@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        list.add(Challenge(UserProfile("Dominik", "dominik@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        list.add(Challenge(UserProfile("Narek", "Narek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        list.add(Challenge(UserProfile("Vašek", "vasek@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        list.add(Challenge(UserProfile("Štěpanisko", "sterba@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        list.add(Challenge(UserProfile("Čonkos", "cokos@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        list.add(Challenge(UserProfile("Čenda", "matka.priroda@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.FASTEST_TIME))
        list.add(Challenge(UserProfile("Chlupata čubka", "apakrychle@fit.cvut.cz"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.HIGHER_MILEAGE))
        return list
    }
}