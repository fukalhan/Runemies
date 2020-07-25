package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_completed.view.*

class CompletedChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var profileImage: ImageView = itemView.profile_image
    var opponentUsername: TextView = itemView.opponent_username
    var resultLayout: LinearLayout = itemView.result_layout
    var userDistance: TextView = itemView.user_distance
    var userResult: TextView = itemView.user_result
    var opponentResultUsername: TextView = itemView.opponent_result_username
    var opponentDistance: TextView = itemView.opponent_distance
    var opponentResult: TextView = itemView.opponent_result
}