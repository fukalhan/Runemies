package cz.cvut.fukalhan.main.enemies.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_enemy.view.*

class EnemyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profileImage: ImageView = itemView.profile_image
    var username: TextView = itemView.username
    var points: TextView = itemView.points
    var buttonPanel: LinearLayout = itemView.button_panel
    var showProfileButton: Button = itemView.profile_button
    var challengeButton: Button = itemView.challenge_button
}