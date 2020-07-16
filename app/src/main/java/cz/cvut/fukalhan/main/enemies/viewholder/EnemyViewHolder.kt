package cz.cvut.fukalhan.main.enemies.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_enemy.view.*

class EnemyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var enemyLayout: LinearLayout = itemView.enemy
    var rank: TextView = itemView.rank
    var username: TextView = itemView.enemy_username
    var points: TextView = itemView.enemy_points
}