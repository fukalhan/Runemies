package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.ChallengesAdapter
import cz.cvut.fukalhan.repository.challenges.state.ChallengeState
import kotlinx.android.synthetic.main.fragment_challenges.*
import kotlinx.android.synthetic.main.profile_user_info.*

class ChallengesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        challengesViewPager.adapter = ChallengesAdapter(this)
        TabLayoutMediator(challengesTabLayout, challengesViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "REQUESTED"
                1 -> "ACTIVE"
                2 -> "COMPLETED"
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    fun acceptChallenge(challengeId: String) {
        val action = ChallengesFragmentDirections.acceptChallenge(challengeState = ChallengeState.ACCEPTED, challengeId = challengeId)
        findNavController().navigate(action)
    }
}
