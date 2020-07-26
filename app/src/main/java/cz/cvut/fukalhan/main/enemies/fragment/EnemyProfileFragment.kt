package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.profile.fragment.ProfileFragment
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_user_info.*

class EnemyProfileFragment : ProfileFragment() {
    private val args: EnemyProfileFragmentArgs by navArgs()

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
