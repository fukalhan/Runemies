package cz.cvut.fukalhan.main.challenges.adapter

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.challenges.viewholder.ActiveChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.Constants
import kotlinx.android.synthetic.main.item_challenge_active.*

class ActiveChallengesAdapter(private val context: Context, private val challenges: List<Challenge>, private val resources: Resources) : RecyclerView.Adapter<ActiveChallengeViewHolder>() {
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_active, parent, false)
        return ActiveChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ActiveChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${challenge.opponentId}")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(context).load(uri).into(holder.profileImage)
            }
        holder.opponentUsername.text = resources.getString(R.string.against_user, challenge.opponentUsername)
        holder.itemView.setOnClickListener {
            if (holder.resultPanel.visibility == View.GONE) {
                holder.resultPanel.visibility = View.VISIBLE
            } else {
                holder.resultPanel.visibility = View.GONE
            }
        }
        holder.yourResult.text = resources.getString(R.string.your_result_n_1_s_km, challenge.challengerDistance.toString())
        holder.opponentResult.text = resources.getString(R.string.waiting_for_result, challenge.opponentUsername)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}