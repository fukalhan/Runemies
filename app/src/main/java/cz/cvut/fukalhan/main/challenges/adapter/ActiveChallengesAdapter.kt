package cz.cvut.fukalhan.main.challenges.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.challenges.viewholder.ActiveChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge

class ActiveChallengesAdapter(private val challenges: List<Challenge>, private val resources: Resources) : RecyclerView.Adapter<ActiveChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_active, parent, false)
        return ActiveChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ActiveChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        holder.userDistance.text = challenge.challengerDistance.toString()
        holder.enemyUsername.text = resources.getString(R.string.enemy_username, challenge.opponentUsername)
        holder.enemyDistance.text = resources.getString(R.string.waiting_for)
        holder.quitChallengeButton.setOnClickListener {
        }
    }
}