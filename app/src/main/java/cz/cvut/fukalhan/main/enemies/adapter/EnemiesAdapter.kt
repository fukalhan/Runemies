package cz.cvut.fukalhan.main.enemies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.enemies.fragment.EnemiesFragment
import cz.cvut.fukalhan.main.enemies.viewholder.EnemyViewHolder
import cz.cvut.fukalhan.repository.entity.User

class EnemiesAdapter(private val fragment: EnemiesFragment, private val enemies: List<User>) : RecyclerView.Adapter<EnemyViewHolder>() {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val ranking get() = setRanking()

    private fun setRanking(): IntArray {
        val ranking = IntArray(itemCount)
        var rank = 1
        ranking[0] = rank
        var previousPoints = enemies[0].points
        for (i in 1 until itemCount) {
            if (previousPoints != enemies[i].points) {
                rank++
            }
            ranking[i] = rank
            previousPoints = enemies[i].points
        }
        return ranking
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnemyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_enemy, parent, false)
        return EnemyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return enemies.size
    }

    override fun onBindViewHolder(holder: EnemyViewHolder, position: Int) {
        val enemy = enemies[position]
        user?.let {
            if (user.uid == enemy.id) {
                holder.enemyLayout.background = fragment.resources.getDrawable(R.drawable.user_background)
            }
        }
        holder.rank.text = "${ranking[position]}."
        holder.username.text = enemy.username
        holder.points.text = enemy.points.toString()
        holder.itemView.setOnClickListener {
            fragment.showEnemyProfile(enemy.id)
        }
    }
}