package cz.cvut.fukalhan.main.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.utils.TimeFormatter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_activity_summary.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), ILoginNavigation {
    private lateinit var profileViewModel: ProfileViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ProfileViewModel()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    /** Request data of current user and set observer for the answer */
    private fun getUserData() {
        profileViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            when {
                user.error -> Toast.makeText(context, "Cannot retrieve user data", Toast.LENGTH_SHORT).show()
                user.data == null -> Toast.makeText(context, "User doesn't exists", Toast.LENGTH_SHORT).show()
                else -> setUserData(user.data)
            }
        })
        // If there is current user signed in request for his data by his ID
        user?.let {
            profile_image.setImageURI(user.photoUrl)
            profile_username.text = user.displayName
            join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.metadata?.creationTimestamp))
            profileViewModel.getUser(it.uid)
        }
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData(user: User) {
        lives.text = user.lives.toString()
        points.text = user.points.toString()
        total_mileage.text = getString(R.string.total_mileage, String.format("%.2f", user.totalMileage))
        total_hours.text = getString(R.string.total_time, TimeFormatter.toHourMinSec(user.totalTime))
        longest_run.text = getString(R.string.longest_run, user.longestRun.toString())
    }
}
