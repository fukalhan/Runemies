package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.ChallengeAdapter
import cz.cvut.fukalhan.main.challenges.viewmodel.ActiveChallengesViewModel
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.android.synthetic.main.fragment_active_challenges.*

/**
 * A simple [Fragment] subclass.
 */
class ActiveChallengesFragment : Fragment() {

    private lateinit var challengesViewModel: ActiveChallengesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        challengesViewModel = ViewModelProviders.of(this).get(ActiveChallengesViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengesViewModel.challenges.observe(viewLifecycleOwner, Observer {challenges ->
            challenges.forEach{ Log.d("ActiveChallenges", "${it}" )}
            setAdapter(challenges)
        })
        challengesViewModel.getChallenges()
    }

    private fun setAdapter(challenges: List<Challenge>) {
        val challengeAdapter = ChallengeAdapter(challenges)
        val viewManager = LinearLayoutManager(activity)
        activeChallengesRecyclerView.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = challengeAdapter
        }
    }
}
