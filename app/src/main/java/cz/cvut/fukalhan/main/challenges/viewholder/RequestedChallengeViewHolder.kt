package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_requested.view.*

class RequestedChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var challengerUsername: TextView = itemView.enemy_username
    var challengeDistance: TextView = itemView.challenge_distance
    var startChallengeButton: Button = itemView.start_challenge
}