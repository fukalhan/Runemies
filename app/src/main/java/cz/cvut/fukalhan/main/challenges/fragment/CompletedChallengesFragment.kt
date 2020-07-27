package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.CompletedChallengesAdapter
import cz.cvut.fukalhan.main.challenges.viewmodel.CompletedChallengesViewModel
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.android.synthetic.main.fragment_completed_challenges.*

class CompletedChallengesFragment : Fragment() {
    private lateinit var challengesViewModel: CompletedChallengesViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        challengesViewModel = CompletedChallengesViewModel()
        return inflater.inflate(R.layout.fragment_completed_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChallenges()
        user?.let { challengesViewModel.getChallenges(it.uid) }
    }

    private fun observeChallenges() {
        challengesViewModel.challenges.observe(viewLifecycleOwner, Observer { challenges ->
            when {
                challenges.error -> Toast.makeText(context, "Cannot retrieve challenges data", Toast.LENGTH_SHORT).show()
                challenges.data.isNullOrEmpty() -> challenges_count.text = getString(R.string.no_completed_challenges)
                else -> {
                    if (challenges.data.size == 1) {
                        challenges_count.text = getString(R.string.one_challenge, challenges.data.size)
                    } else {
                        challenges_count.text = getString(R.string.challenges_count, challenges.data.size)
                    }
                    setAdapter(challenges.data)
                }
            }
        })
    }

    private fun setAdapter(challenges: List<Challenge>) {
        val challengeAdapter = context?.let { context -> CompletedChallengesAdapter(requireContext(), challenges, context.resources) }
        val viewManager = LinearLayoutManager(activity)
        completedChallengesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = challengeAdapter
        }
    }
}
