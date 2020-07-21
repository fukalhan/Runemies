package cz.cvut.fukalhan.repository.challenges

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import cz.cvut.fukalhan.repository.challenges.state.ChallengeState
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.DataWrapper
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ChallengesRepository : IChallengesRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun createChallenge(challenge: Challenge) {
        try {
            val id = db.collection(Constants.CHALLENGES).document().id
            db.collection(Constants.CHALLENGES).document(id).set(challenge).await()
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    override suspend fun getActiveChallenges(userId: String): DataWrapper<List<Challenge>> {
        return try {
            val challenges = ArrayList<Challenge>()
            val snapshot = db.collection(Constants.CHALLENGES).get().await()
            snapshot.documents.forEach { doc ->
                val challenge = doc.toObject(Challenge::class.java)
                challenge?.let {
                    if (challenge.opponentId == userId && challenge.state == ChallengeState.ACTIVE) {
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

    override suspend fun getCompletedChallenges(userId: String): DataWrapper<List<Challenge>> {
        return try {
            val challenges = ArrayList<Challenge>()
            val snapshot = db.collection(Constants.CHALLENGES).get().await()
            snapshot.documents.forEach { doc ->
                val challenge = doc.toObject(Challenge::class.java)
                challenge?.let {
                    if (challenge.challengerId == userId || (challenge.opponentId == userId && challenge.state == ChallengeState.COMPLETED)) {
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