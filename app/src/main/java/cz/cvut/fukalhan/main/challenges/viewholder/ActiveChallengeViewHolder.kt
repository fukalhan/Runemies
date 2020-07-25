package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_active.view.*

class ActiveChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var profileImage: ImageView = itemView.profile_image
    var opponentUsername: TextView = itemView.opponent_username
    var yourResult: TextView = itemView.your_result
    var opponentResult: TextView = itemView.waiting_for_opponent
    var quitChallengeButton: Button = itemView.quit_challenge
    var resultButtonPanel: RelativeLayout = itemView.result_and_button_panel
}