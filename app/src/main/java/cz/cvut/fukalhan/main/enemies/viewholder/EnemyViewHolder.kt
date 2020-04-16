package cz.cvut.fukalhan.main.enemies.viewholder

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_enemy.view.*

class EnemyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var username: TextView = itemView.enemyUsername
}