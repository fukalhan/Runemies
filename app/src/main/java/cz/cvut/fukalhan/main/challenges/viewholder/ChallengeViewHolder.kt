package cz.cvut.fukalhan.main.challenges.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.repository.entity.ChallengeType
import kotlinx.android.synthetic.main.item_challenge.view.*
import java.util.*

class ChallengeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var username: TextView = itemView.challengerUsername
    var duration: TextView = itemView.challengeDuration
    var type: TextView = itemView.challengeType
}