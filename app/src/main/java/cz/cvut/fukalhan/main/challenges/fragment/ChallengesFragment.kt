package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.ChallengesAdapter
import kotlinx.android.synthetic.main.fragment_challenges.*

/**
 * A simple [Fragment] subclass.
 */
class ChallengesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        challengesViewPager.adapter = ChallengesAdapter(this)
        TabLayoutMediator(challengesTabLayout, challengesViewPager) { tab, position ->
            tab.text = when(position){
                0 -> "ACTIVE"
                1 -> "REQUESTED"
                else -> throw IllegalStateException()
            }
        }.attach()
    }
}
