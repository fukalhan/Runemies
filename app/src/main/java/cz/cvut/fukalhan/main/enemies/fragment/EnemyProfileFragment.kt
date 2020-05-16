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
import kotlinx.android.synthetic.main.profile_activity_records.*
import kotlinx.android.synthetic.main.profile_activity_summary.*

/**
 * A simple [Fragment] subclass.
 */
class EnemyProfileFragment : Fragment() {
    val args: EnemyProfileFragmentArgs by navArgs()
    private lateinit var viewModel: EnemyProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = EnemyProfileViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enemy_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.enemy.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                setUserData(user)
            } else {
                Toast.makeText(context, "Enemy data not available", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getEnemy(args.enemyID)
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData(user: User) {
        enemy_profile_username.text = user.username
        enemy_profile_join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.joinDate))
        enemy_profile_points.text = user.points.toString()
        total_mileage.text = getString(R.string.total_mileage, user.totalMileage.toString())
        total_hours.text = getString(R.string.total_time, user.totalTime.toString())
        longest_run.text = getString(R.string.longest_run, user.longestRun.toString())
        fastest_1_km.text = getString(R.string.fastest_1_km, TimeFormatter.toMinSec(user.fastest1km))
    }
}
