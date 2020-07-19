package cz.cvut.fukalhan.main.profile.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.repository.entity.ActivityStatistics
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.TimeFormatter
import kotlinx.android.synthetic.main.profile_activity_summary.*
import kotlinx.android.synthetic.main.profile_user_info.*

open class ProfileFragment : Fragment(), ILoginNavigation {
    protected lateinit var profileViewModel: ProfileViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ProfileViewModel()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()
    }

    /** Request data of current user and set observer for the answer */
    protected open fun setUserData() {
        profileViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            when {
                user.error -> Toast.makeText(context, "Cannot retrieve user data", Toast.LENGTH_SHORT).show()
                user.data == null -> Toast.makeText(context, "User doesn't exists", Toast.LENGTH_SHORT).show()
                else -> setUserData(user.data)
            }
        })

        profileViewModel.userStatistics.observe(viewLifecycleOwner, Observer {
            setUserStatistics(it)
        })

        getUserData()
    }

    protected open fun getUserData() {
        user?.let {
            setProfileImage(it.uid)
            username.text = user.displayName
            join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.metadata?.creationTimestamp))
            profileViewModel.getUserData(it.uid)
            profileViewModel.getUserRunStatistics(it.uid)
        }
    }

    protected fun setProfileImage(userId: String) {
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}$userId")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(requireContext()).load(uri).into(profile_image)
            }
    }

    /** Set data of given user on profile fragment screen */
    protected open fun setUserData(user: User) {
        lives.text = user.lives.toString()
        points.text = user.points.toString()
    }

    private fun setUserStatistics(statistics: ActivityStatistics) {
        total_mileage.text = getString(R.string.total_mileage, String.format("%.2f", statistics.totalMileage))
        total_hours.text = getString(R.string.total_time, TimeFormatter.toHourMinSec(statistics.totalTime))
        longest_run.text = getString(R.string.longest_run, statistics.longestRun.toString())
    }
}
