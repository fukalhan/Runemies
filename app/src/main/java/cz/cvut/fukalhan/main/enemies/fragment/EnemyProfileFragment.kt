package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.profile.fragment.ProfileFragment
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_user_info.*

class EnemyProfileFragment : ProfileFragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private val args: EnemyProfileFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_out_layout.visibility = View.GONE
        image_setting.visibility = View.GONE
    }
    override fun getUserData() {
        setProfileImage(args.enemyID)
        profileViewModel.getUserData(args.enemyID)
        profileViewModel.getUserStatistics(args.enemyID)
    }

    /** Set data of given user on profile fragment screen */
    override fun setUserData(user: User) {
        username.text = user.username
        join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.joinDate))
        points.text = user.points.toString()
    }
}
