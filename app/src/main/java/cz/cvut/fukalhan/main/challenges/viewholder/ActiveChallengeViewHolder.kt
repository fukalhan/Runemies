package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_active.view.*

class ActiveChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var profileImage: ImageView = itemView.profile_image
    var opponentUsername: TextView = itemView.opponent_username
    var yourResult: TextView = itemView.your_result
    var opponentResult: TextView = itemView.waiting_for_opponent
    var resultPanel: LinearLayout = itemView.result_panel
}