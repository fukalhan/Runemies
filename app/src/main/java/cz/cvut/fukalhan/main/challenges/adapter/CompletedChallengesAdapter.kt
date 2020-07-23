package cz.cvut.fukalhan.main.challenges.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.viewholder.CompletedChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.utils.TimeFormatter

class CompletedChallengesAdapter(private val challenges: List<Challenge>, private val resources: Resources) : RecyclerView.Adapter<CompletedChallengeViewHolder>() {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_completed, parent, false)
        return CompletedChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: CompletedChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        user?.let {
            when (it.uid) {
                challenge.challengerId -> {
                    holder.userDistance.text = resources.getString(R.string.challenge_distance, challenge.challengerDistance.toString())
                    holder.opponentUsername.text = resources.getString(R.string.enemy_username, challenge.opponentUsername)
                    holder.opponentDistance.text = resources.getString(R.string.challenge_distance, challenge.opponentDistance.toString())
                    when {
                        challenge.challengerDistance == challenge.opponentDistance -> {
                            holder.userResult.text = resources.getString(R.string.tie)
                            holder.userResult.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            holder.opponentResult.text = resources.getString(R.string.tie)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        }
                        challenge.challengerDistance < challenge.opponentDistance -> {
                            holder.userResult.text = resources.getString(R.string.looser)
                            holder.userResult.setTextColor(resources.getColor(R.color.red))
                            holder.opponentResult.text = resources.getString(R.string.winner)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.green))
                        }
                        else -> {
                            holder.userResult.text = resources.getString(R.string.winner)
                            holder.userResult.setTextColor(resources.getColor(R.color.green))
                            holder.opponentResult.text = resources.getString(R.string.looser)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.red))
                        }
                    }
                }
                challenge.opponentId -> {
                    holder.userDistance.text = resources.getString(R.string.challenge_distance, challenge.opponentDistance.toString())
                    holder.opponentUsername.text = resources.getString(R.string.enemy_username, challenge.challengerUsername)
                    holder.opponentDistance.text = resources.getString(R.string.challenge_distance, challenge.challengerDistance.toString())
                    when {
                        challenge.challengerDistance == challenge.opponentDistance -> {
                            holder.userResult.text = resources.getString(R.string.tie)
                            holder.userResult.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            holder.opponentResult.text = resources.getString(R.string.tie)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        }
                        challenge.challengerDistance < challenge.opponentDistance -> {
                            holder.userResult.text = resources.getString(R.string.winner)
                            holder.userResult.setTextColor(resources.getColor(R.color.green))
                            holder.opponentResult.text = resources.getString(R.string.looser)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.red))
                        }
                        else -> {
                            holder.userResult.text = resources.getString(R.string.looser)
                            holder.userResult.setTextColor(resources.getColor(R.color.red))
                            holder.opponentResult.text = resources.getString(R.string.winner)
                            holder.opponentResult.setTextColor(resources.getColor(R.color.green))
                        }
                    }
                }
            }
        }
    }
}