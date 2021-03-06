package cz.cvut.fukalhan.main.challenges.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.viewholder.CompletedChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.utils.ViewVisibility

class CompletedChallengesAdapter(private val challenges: List<Challenge>) : RecyclerView.Adapter<CompletedChallengeViewHolder>() {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_completed, parent, false)
        return CompletedChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: CompletedChallengeViewHolder, position: Int) {
        val context = holder.itemView.context
        val challenge = challenges[position]
        var userDistance: Double = 0.0
        var opponentId: String = ""
        var opponentUsername: String = ""
        var opponentDistance: Double = 0.0
        user?.let {
            when (it.uid) {
                challenge.challengerId -> {
                    userDistance = challenge.challengerDistance
                    opponentId = challenge.opponentId
                    opponentUsername = challenge.opponentUsername
                    opponentDistance = challenge.opponentDistance
                }
                else -> {
                    userDistance = challenge.opponentDistance
                    opponentId = challenge.challengerId
                    opponentUsername = challenge.challengerUsername
                    opponentDistance = challenge.challengerDistance
                }
            }
        }
        holder.startDate.text = context.resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}$opponentId")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(context).load(uri).into(holder.profileImage)
            }
        holder.opponentUsername.text = context.resources.getString(R.string.against_user, opponentUsername)
        holder.itemView.setOnClickListener {
            ViewVisibility.toggleVisibility(holder.resultLayout)
        }
        holder.userDistance.text = context.resources.getString(R.string.challenge_distance, userDistance.toString())
        holder.opponentResultUsername.text = context.resources.getString(R.string.opponent_username_result, opponentUsername)
        holder.opponentDistance.text = context.resources.getString(R.string.challenge_distance, opponentDistance.toString())

        when {
            userDistance == opponentDistance -> {
                holder.userResult.text = context.resources.getString(R.string.tie)
                holder.userResult.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
                holder.opponentResult.text = context.resources.getString(R.string.tie)
                holder.opponentResult.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            }
            userDistance > opponentDistance -> {
                holder.userResult.text = context.resources.getString(R.string.winner)
                holder.userResult.setTextColor(context.resources.getColor(R.color.green))
                holder.opponentResult.text = context.resources.getString(R.string.looser)
                holder.opponentResult.setTextColor(context.resources.getColor(R.color.red))
            }
            else -> {
                holder.userResult.text = context.resources.getString(R.string.looser)
                holder.userResult.setTextColor(context.resources.getColor(R.color.red))
                holder.opponentResult.text = context.resources.getString(R.string.winner)
                holder.opponentResult.setTextColor(context.resources.getColor(R.color.green))
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}