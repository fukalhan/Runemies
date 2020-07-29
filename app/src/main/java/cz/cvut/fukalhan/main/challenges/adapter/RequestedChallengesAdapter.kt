package cz.cvut.fukalhan.main.challenges.adapter

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.fragment.RequestedChallengesFragment
import cz.cvut.fukalhan.main.challenges.viewholder.RequestedChallengeViewHolder
import cz.cvut.fukalhan.repository.entity.Challenge
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.TimeFormatter
import kotlinx.android.synthetic.main.item_challenge_requested.view.*

class RequestedChallengesAdapter(private val context: Context, private val fragment: RequestedChallengesFragment, private val challenges: ArrayList<Challenge>, private val resources: Resources) : RecyclerView.Adapter<RequestedChallengeViewHolder>() {
    private val storageRef: StorageReference = Firebase.storage.reference
    val requestsCount: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    init {
        requestsCount.postValue(challenges.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestedChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_requested, parent, false)
        return RequestedChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: RequestedChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.startDate.text = resources.getString(R.string.challenge_date, TimeFormatter.simpleDate.format(challenge.startDate))
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${challenge.challengerId}")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(context).load(uri).into(holder.profileImage)
            }
        holder.challengerUsername.text = resources.getString(R.string.challenged_by, challenge.challengerUsername)
        holder.guess.text = resources.getString(R.string.guess, challenge.challengerUsername)
        holder.challengeActionButton.setOnClickListener {
            fragment.challengeActionDialog(challenge.id, position)
        }
        holder.itemView.setOnClickListener {
            if (holder.buttonPanel.visibility == View.GONE) {
                holder.buttonPanel.visibility = View.VISIBLE
            } else {
                holder.buttonPanel.visibility = View.GONE
            }
        }
    }

    fun deleteChallengeAtPosition(position: Int) {
        challenges.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, challenges.size)
        this.notifyDataSetChanged()
        requestsCount.postValue(challenges.size)
    }
}