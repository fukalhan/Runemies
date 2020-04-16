package cz.cvut.fukalhan.main.enemies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.IViewHolderClickListener
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.main.enemies.fragment.EnemiesFragment
import cz.cvut.fukalhan.main.enemies.fragment.SingleEnemyFragment
import cz.cvut.fukalhan.main.enemies.viewholder.EnemyViewHolder
import cz.cvut.fukalhan.repository.entity.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class EnemiesAdapter(private val fragment: EnemiesFragment, private val enemies: List<User>): RecyclerView.Adapter<EnemyViewHolder>(), KoinComponent{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnemyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_enemy, parent, false)
        return EnemyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return enemies.size
    }

    override fun onBindViewHolder(holder: EnemyViewHolder, position: Int) {
        val enemy = enemies[position]
        holder.username.text = enemy.username
        holder.itemView.setOnClickListener {
            fragment.showEnemy(enemy.id)
        }
    }
}