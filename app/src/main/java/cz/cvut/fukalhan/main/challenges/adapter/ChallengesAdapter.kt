package cz.cvut.fukalhan.main.challenges.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.cvut.fukalhan.main.challenges.fragment.ActiveChallengesFragment
import cz.cvut.fukalhan.main.challenges.fragment.ChallengesFragment
import cz.cvut.fukalhan.main.challenges.fragment.CompletedChallengesFragment
import cz.cvut.fukalhan.main.challenges.fragment.RequestedChallengesFragment

class ChallengesAdapter(private val challengesFragment: ChallengesFragment) : FragmentStateAdapter(challengesFragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RequestedChallengesFragment(challengesFragment)
            1 -> ActiveChallengesFragment()
            2 -> CompletedChallengesFragment()
            else -> throw IllegalStateException()
        }
    }
}