package cz.cvut.fukalhan.main.enemies.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.enemies.fragment.EnemiesFragment
import cz.cvut.fukalhan.main.enemies.viewholder.EnemyViewHolder
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.ViewVisibility

class EnemiesAdapter(private val fragment: EnemiesFragment, private val enemies: List<User>) : RecyclerView.Adapter<EnemyViewHolder>() {
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnemyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_enemy, parent, false)
        return EnemyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return enemies.size
    }

    override fun onBindViewHolder(holder: EnemyViewHolder, position: Int) {
        val context = holder.itemView.context
        val enemy = enemies[position]
        holder.itemView.setOnClickListener {
            ViewVisibility.toggleVisibility(holder.buttonPanel)
        }
        holder.showProfileButton.setOnClickListener {
            fragment.showEnemyProfile(enemy.id)
        }
        holder.challengeButton.setOnClickListener {
            fragment.challengeUser(enemy.id, enemy.username)
        }

        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${enemy.id}")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(context).load(uri).into(holder.profileImage)
            }
        holder.username.text = enemy.username
        holder.points.text = enemy.points.toString()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}