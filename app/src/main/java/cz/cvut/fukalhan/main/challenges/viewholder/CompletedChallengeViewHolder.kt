package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_completed.view.*

class CompletedChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var userDistance: TextView = itemView.user_distance
    var opponentUsername: TextView = itemView.enemy_username
    var opponentDistance: TextView = itemView.enemy_distance
    var userResult: TextView = itemView.user_result
    var opponentResult: TextView = itemView.enemy_result
}