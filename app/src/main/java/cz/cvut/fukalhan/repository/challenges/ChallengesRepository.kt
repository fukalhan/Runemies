package cz.cvut.fukalhan.repository.challenges

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.challenges.state.ChallengeDeleteState
import cz.cvut.fukalhan.repository.challenges.state.ChallengeState
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ChallengesRepository : IChallengesRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun createChallenge(challenge: Challenge) {
        try {
            val id = db.collection(Constants.CHALLENGES).document().id
            challenge.id = id
            db.collection(Constants.CHALLENGES).document(id).set(challenge).await()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    override suspend fun updateChallenge(challengeId: String, userId: String, userDistance: Double) {
        try {
            val snapshot = db.collection(Constants.CHALLENGES).document(challengeId).get().await()
            if (snapshot.exists()) {
                val challenge = snapshot.toObject(Challenge::class.java)
                challenge?.let {
                    var opponentId = ""
                    var opponentDistance = 0.0
                    when (userId) {
                        challenge.challengerId -> {
                            opponentId = challenge.opponentId
                            opponentDistance = challenge.opponentDistance
                        }
                        else -> {
                            opponentId = challenge.challengerId
                            opponentDistance = challenge.challengerDistance
                        }
                    }
                    when {
                        userDistance == opponentDistance -> {
                            updatePoints(userId, 1)
                            updatePoints(opponentId, 1)
                        }
                        userDistance > opponentDistance -> {
                            updatePoints(userId, 1)
                            updatePoints(opponentId, -1)
                        }
                        else -> {
                            updatePoints(userId, -1)
                            updatePoints(opponentId, 1)
                        }
                    }
                    challenge.state = ChallengeState.COMPLETED
                    db.collection(Constants.CHALLENGES).document(challengeId).set(challenge).await()
                }
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    override suspend fun deleteChallenge(challengeId: String): ChallengeDeleteState {
        return try {
            db.collection(Constants.CHALLENGES).document(challengeId).delete().await()
            ChallengeDeleteState.SUCCESS
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            ChallengeDeleteState.FAIL
        }
    }

    private suspend fun updatePoints(userId: String, points: Int) {
        try {
            val snapshot = db.collection(Constants.USERS).document(userId).get().await()
            if (snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null && (user.points != 0 || points > 0)) {
                    user.points += points
                    db.collection(Constants.USERS).document(userId).set(user).await()
                }
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    override suspend fun getRequestedChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return try {
            val challenges = ArrayList<Challenge>()
            val snapshot = db.collection(Constants.CHALLENGES).get().await()
            snapshot.documents.forEach { doc ->
                val challenge = doc.toObject(Challenge::class.java)
                challenge?.let {
                    if (challenge.opponentId == userId && challenge.state == ChallengeState.STARTED) {
                        challenges.add(it)
                    }
                }
            }
            DataWrapper(challenges, false)
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            DataWrapper(null, true, e.message)
        }
    }

    override suspend fun getActiveChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return try {
            val challenges = ArrayList<Challenge>()
            val snapshot = db.collection(Constants.CHALLENGES).get().await()
            snapshot.documents.forEach { doc ->
                val challenge = doc.toObject(Challenge::class.java)
                challenge?.let {
                    if (challenge.challengerId == userId && challenge.state == ChallengeState.STARTED) {
                        challenges.add(it)
                    }
                }
            }
            DataWrapper(challenges, false)
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            DataWrapper(null, true, e.message)
        }
    }

    override suspend fun getCompletedChallenges(userId: String): DataWrapper<ArrayList<Challenge>> {
        return try {
            val challenges = ArrayList<Challenge>()
            val snapshot = db.collection(Constants.CHALLENGES).get().await()
            snapshot.documents.forEach { doc ->
                val challenge = doc.toObject(Challenge::class.java)
                challenge?.let {
                    if ((challenge.challengerId == userId || challenge.opponentId == userId) && challenge.state == ChallengeState.COMPLETED) {
                        challenges.add(it)
                    }
                }
            }
            DataWrapper(challenges, false)
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            DataWrapper(null, true, e.message)
        }
    }
}