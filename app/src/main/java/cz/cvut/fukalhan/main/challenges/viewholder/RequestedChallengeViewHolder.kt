package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.item_challenge_requested.view.*

class RequestedChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var startDate: TextView = itemView.start_date
    var profileImage: ImageView = itemView.profile_image
    var challengerUsername: TextView = itemView.challenger_username
    var buttonPanel: LinearLayout = itemView.button_panel
    var guess: TextView = itemView.guess
    var challengeActionButton: FloatingActionButton = itemView.challenge_action_button
}