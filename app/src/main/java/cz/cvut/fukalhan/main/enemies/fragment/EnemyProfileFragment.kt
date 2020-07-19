package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.repository.entity.ActivityStatistics
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_enemy_profile.*
import kotlinx.android.synthetic.main.profile_activity_summary.*

/**
 * A simple [Fragment] subclass.
 */
class EnemyProfileFragment : Fragment() {
    private val args: EnemyProfileFragmentArgs by navArgs()
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ProfileViewModel()
        return inflater.inflate(R.layout.fragment_enemy_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        profileViewModel.getUserData(args.enemyID)
        profileViewModel.getUserRunStatistics(args.enemyID)
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData(user: User) {
        enemy_profile_username.text = user.username
        enemy_profile_join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.joinDate))
        enemy_profile_points.text = user.points.toString()
    }

    private fun setUserStatistics(statistics: ActivityStatistics) {
        total_mileage.text = getString(R.string.total_mileage, String.format("%.2f", statistics.totalMileage))
        total_hours.text = getString(R.string.total_time, TimeFormatter.toHourMinSec(statistics.totalTime))
        longest_run.text = getString(R.string.longest_run, statistics.longestRun.toString())
    }
}
