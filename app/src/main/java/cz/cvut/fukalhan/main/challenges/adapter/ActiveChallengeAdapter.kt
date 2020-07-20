package cz.cvut.fukalhan.main.challenges.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.challenges.viewholder.ChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge

class ActiveChallengeAdapter(private val challenges: List<Challenge>, private val resources: Resources) : RecyclerView.Adapter<ChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        // holder.username.text = challenge.cha.username
        holder.distance.text = resources.getString(R.string.challenge_distance, challenge.distance.toString())
    }
}