package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.RequestedChallengesAdapter
import cz.cvut.fukalhan.main.challenges.dialog.ChallengeActionDialog
import cz.cvut.fukalhan.main.challenges.dialog.IChallengeActionListener
import cz.cvut.fukalhan.main.challenges.viewmodel.RequestedChallengesViewModel
import cz.cvut.fukalhan.repository.challenges.state.ChallengeDeleteState
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.android.synthetic.main.fragment_requested_challenges.*

class RequestedChallengesFragment(private val challengesFragment: ChallengesFragment) : Fragment(), IChallengeActionListener {
    private lateinit var challengesViewModel: RequestedChallengesViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var challengesAdapter: RequestedChallengesAdapter? = null
    private var position: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        challengesViewModel = ViewModelProvider(requireActivity()).get(RequestedChallengesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_requested_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChallenges()
        observeChallengeDeleteState()
        user?.let { challengesViewModel.getChallenges(it.uid) }
    }

    private fun observeChallenges() {
        challengesViewModel.challenges.observe(viewLifecycleOwner, Observer { challenges ->
            when {
                challenges.error -> Toast.makeText(context, "Cannot retrieve challenge requests", Toast.LENGTH_SHORT).show()
                else -> setAdapter(challenges.data!!)
            }
        })
    }

    private fun observeChallengeDeleteState() {
        challengesViewModel.challengeDeleteState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ChallengeDeleteState.SUCCESS -> {
                    if (position >= 0) {
                        requestedChallengesRecyclerView.removeViewAt(position)
                        challengesAdapter?.deleteChallengeAtPosition(position)
                        position = -1
                    }
                }
                ChallengeDeleteState.FAIL -> Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(challenges: ArrayList<Challenge>) {
        challengesAdapter = context?.let { context -> RequestedChallengesAdapter(requireContext(), this, challenges, context.resources) }
        val viewManager = LinearLayoutManager(activity)
        requestedChallengesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = challengesAdapter
        }
        observeChallengesCount()
    }

    private fun observeChallengesCount() {
        challengesAdapter?.requestsCount?.observe(viewLifecycleOwner, Observer { count ->
            when (count) {
                0 -> challenges_count.text = getString(R.string.no_requested_challenges)
                1 -> challenges_count.text = getString(R.string.one_request, count)
                else -> challenges_count.text = getString(R.string.requests_count, count)
            }
        })
    }

    fun challengeActionDialog(challengeId: String, position: Int) {
        val dialog = ChallengeActionDialog(this as IChallengeActionListener, challengeId, position)
        dialog.show(requireFragmentManager(), "AcceptChallengeDialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, challengeId: String) {
        challengesFragment.acceptChallenge(challengeId)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment, challengeId: String, deletePosition: Int) {
        position = deletePosition
        challengesViewModel.deleteChallenge(challengeId)
    }
}