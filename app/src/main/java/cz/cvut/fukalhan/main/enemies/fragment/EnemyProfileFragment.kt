package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.enemies.dialog.ChallengeUserDialog
import cz.cvut.fukalhan.main.enemies.dialog.IChallengeUserListener
import cz.cvut.fukalhan.main.profile.fragment.ProfileFragment
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.profile_user_info.*

class EnemyProfileFragment : ProfileFragment(), IChallengeUserListener {
    private val args: EnemyProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challenge_user.visibility = View.VISIBLE
        challenge_user.setOnClickListener {
            challengeUser()
        }
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

    private fun challengeUser() {
        val dialog = ChallengeUserDialog(this as IChallengeUserListener)
        dialog.show(requireFragmentManager(), "ChallengeUserDialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        startChallenge(true, args.enemyID)
    }

    fun startChallenge(challengeStarted: Boolean, enemyId: String) {
        val action = EnemyProfileFragmentDirections.startChallenge(challengeStarted, enemyId)
        findNavController().navigate(action)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}
