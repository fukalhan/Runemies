package cz.cvut.fukalhan.main.challenges.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.cvut.fukalhan.main.challenges.fragment.ActiveChallengesFragment
import cz.cvut.fukalhan.main.challenges.fragment.ChallengesFragment
import cz.cvut.fukalhan.main.challenges.fragment.RequestedChallengesFragment

class ChallengesAdapter(challengesFragment: ChallengesFragment) : FragmentStateAdapter(challengesFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveChallengesFragment()
            1 -> RequestedChallengesFragment()
            else -> throw IllegalStateException()
        }
    }
}