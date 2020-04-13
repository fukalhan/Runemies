package cz.cvut.fukalhan.main.challenges.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.viewholder.ChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge

class ChallengeAdapter(private val challenges: List<Challenge>): RecyclerView.Adapter<ChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.username.text = challenge.oponent.username
        holder.duration.text = "${challenge.startDate} - ${challenge.endDate}"
        holder.type.text = challenge.type.toString()
    }
}