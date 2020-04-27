package cz.cvut.fukalhan.main.profile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.common.TimeFormatter
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), ILoginNavigation {

    private lateinit var profileViewModel: ProfileViewModel
    val userAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ProfileViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    /** Request data of current user and set observer for the answer */
    private fun getUserData() {
        profileViewModel.user.observe(viewLifecycleOwner, Observer { userData ->
            if (userData != null) {
                setUserData(userData)
            } else {
                Toast.makeText(context, "User data not available", Toast.LENGTH_SHORT).show()
            }
        })
        // If there is current user signed in request for his data by his ID
        if (userAuth != null) {
            profileViewModel.getUser(userAuth.uid)
        }
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData(user: User) {
        profile_username.text = user.username
        join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.joinDate))
        lives.text = user.lives.toString()
        points.text = user.points.toString()
        total_mileage.text = getString(R.string.total_mileage, user.totalMileage.toString())
        total_hours.text = getString(R.string.total_time, user.totalTime.toString())
        longest_run.text = getString(R.string.longest_run, user.longestRun.toString())
        fastest_1_km.text = getString(R.string.fastest_1_km, TimeFormatter.toMinSec(user.fastest1km))
    }
}
