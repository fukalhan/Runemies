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
import cz.cvut.fukalhan.main.enemies.viewmodel.SingleEnemyViewModel
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_single_enemy.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class SingleEnemyFragment : Fragment() {
    val args: SingleEnemyFragmentArgs by navArgs()
    private lateinit var singleEnemyViewModel: SingleEnemyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        singleEnemyViewModel = SingleEnemyViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_enemy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singleEnemyViewModel.enemy.observe(viewLifecycleOwner, Observer {user ->
            if (user != null) {
                setUserData(user)
            } else {
                Toast.makeText(context, "Enemy data not available", Toast.LENGTH_SHORT).show()
            }
        })
        singleEnemyViewModel.getEnemy(args.enemyID)
    }

    /** Set data of given user on profile fragment screen */
    private fun setUserData (user: User) {
        enemy_profile_username.text = user.username
        val formater = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        enemy_profile_join_date.text = getString(R.string.joined, formater.format(user.joinDate))
        enemy_profile_points.text = user.points.toString()
        enemy_total_mileage.text = getString(R.string.total_mileage, user.totalMileage.toString())
        enemy_total_hours.text = getString(R.string.total_time, user.totalTime.toString())
        enemy_longest_run.text = getString(R.string.longest_run, user.longestRun.toString())
        val fastest1KmTime = String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(user.fastest1km),
            TimeUnit.MILLISECONDS.toSeconds(user.fastest1km) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(user.fastest1km))
        )
        enemy_fastest_1_km.text = getString(R.string.fastest_1_km, fastest1KmTime)
    }
}
