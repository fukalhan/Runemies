package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_active.view.*

class ActiveChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var userDistance: TextView = itemView.user_distance
    var enemyUsername: TextView = itemView.enemy_username
    var enemyDistance: TextView = itemView.enemy_distance
    var quitChallengeButton: Button = itemView.quit_challenge
}