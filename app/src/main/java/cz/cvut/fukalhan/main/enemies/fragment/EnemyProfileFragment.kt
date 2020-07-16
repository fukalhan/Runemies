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
import cz.cvut.fukalhan.main.enemies.viewmodel.EnemyProfileViewModel
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_enemy_profile.*
import kotlinx.android.synthetic.main.profile_activity_summary.*

/**
 * A simple [Fragment] subclass.
 */
class EnemyProfileFragment : Fragment() {
    private val args: EnemyProfileFragmentArgs by navArgs()
    private lateinit var enemyProfileviewModel: EnemyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        enemyProfileviewModel = EnemyProfileViewModel()
        return inflater.inflate(R.layout.fragment_enemy_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enemyProfileviewModel.enemy.observe(viewLifecycleOwner, Observer { user ->
            when {
                user.error -> Toast.makeText(context, "Cannot retrieve user data", Toast.LENGTH_SHORT).show()
                user.data == null -> Toast.makeText(context, "User doesn't exists", Toast.LENGTH_SHORT).show()
                else -> setUserData(user.data)
            }
        })
        enemyProfileviewModel.getEnemy(args.enemyID)
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData(user: User) {
        enemy_profile_username.text = user.username
        enemy_profile_join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.joinDate))
        enemy_profile_points.text = user.points.toString()
        total_mileage.text = getString(R.string.total_mileage, user.totalMileage.toString())
        total_hours.text = getString(R.string.total_time, user.totalTime.toString())
        longest_run.text = getString(R.string.longest_run, user.longestRun.toString())
    }
}
