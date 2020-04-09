package cz.cvut.fukalhan.repository.challenges

import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.ChallengeType
import cz.cvut.fukalhan.repository.entity.User
import java.util.*
import kotlin.collections.ArrayList

class FakeChallengesRepository: IChallengesRepository {
    override suspend fun getChallenges(): List<Challenge> {
        val list = ArrayList<Challenge>()
        list.add(Challenge(User("prvni"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        list.add(Challenge(User("druhy"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        list.add(Challenge(User("treti"), Calendar.getInstance().time, Calendar.getInstance().time, ChallengeType.BIGGER_ELEVATION))
        return list
    }
}