package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge_requested.view.*

class RequestedChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var profileImage: ImageView = itemView.profile_image
    var challengerUsername: TextView = itemView.challenger_username
    var challengeQuestion: TextView = itemView.challenge_question
    var buttonPanel: LinearLayout = itemView.button_panel
    var acceptChallenge: Button = itemView.accept_challenge
    var declineChallenge: Button = itemView.decline_challenge
}