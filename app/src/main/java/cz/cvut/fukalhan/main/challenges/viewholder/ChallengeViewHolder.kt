package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge.view.*

class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var username: TextView = itemView.enemy_username
    var distance: TextView = itemView.challenge_distance
}