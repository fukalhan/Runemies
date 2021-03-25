package cz.cvut.fukalhan.main.challenges.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.viewholder.RequestedChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.utils.ViewVisibility

class RequestedChallengesAdapter(private val challenges: ArrayList<Challenge>, private val challengeListener: IChallengeListener) : RecyclerView.Adapter<RequestedChallengeViewHolder>() {
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestedChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_requested, parent, false)
        return RequestedChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: RequestedChallengeViewHolder, position: Int) {
        val context = holder.itemView.context
        val challenge = challenges[position]
        holder.startDate.text = context.resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${challenge.challengerId}")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(context).load(uri).into(holder.profileImage)
            }
        holder.challengerUsername.text = context.resources.getString(R.string.challenged_by, challenge.challengerUsername)
        holder.guess.text = context.resources.getString(R.string.guess, challenge.challengerUsername)
        holder.challengeActionButton.setOnClickListener {
            challengeListener.onClick(challenge.id, position)
        }
        holder.itemView.setOnClickListener {
            ViewVisibility.toggleVisibility(holder.buttonPanel)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun deleteChallengeAtPosition(position: Int) {
        challenges.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, challenges.size)
        this.notifyDataSetChanged()
    }
}