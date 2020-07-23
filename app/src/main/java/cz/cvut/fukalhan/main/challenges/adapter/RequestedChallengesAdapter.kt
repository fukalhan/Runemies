package cz.cvut.fukalhan.main.challenges.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.fragment.RequestedChallengesFragment
import cz.cvut.fukalhan.main.challenges.viewholder.RequestedChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.utils.TimeFormatter

class RequestedChallengesAdapter(private val fragment: RequestedChallengesFragment, private val challenges: List<Challenge>, private val resources: Resources) : RecyclerView.Adapter<RequestedChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestedChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_requested, parent, false)
        return RequestedChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: RequestedChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        holder.challengerUsername.text = challenge.challengerUsername
        holder.challengeDistance.text = resources.getString(R.string.challenge_distance, challenge.challengerDistance.toString())
        holder.startChallengeButton.setOnClickListener {
            fragment.acceptChallengeDialog(challenge.id)
        }
    }
}