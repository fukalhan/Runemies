package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.IChallengeListener
import cz.cvut.fukalhan.main.challenges.adapter.RequestedChallengesAdapter
import cz.cvut.fukalhan.main.challenges.dialog.ChallengeActionDialog
import cz.cvut.fukalhan.main.challenges.dialog.IChallengeActionListener
import cz.cvut.fukalhan.main.challenges.viewmodel.RequestedChallengesViewModel
import cz.cvut.fukalhan.repository.challenges.state.ChallengeDeleteState
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.android.synthetic.main.fragment_requested_challenges.*

class RequestedChallengesFragment(private val challengesFragment: ChallengesFragment) : Fragment(), IChallengeActionListener, IChallengeListener {
    private lateinit var challengesViewModel: RequestedChallengesViewModel
    private lateinit var challengesAdapter: RequestedChallengesAdapter
    private var deletePosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        challengesViewModel = ViewModelProvider(requireActivity()).get(RequestedChallengesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_requested_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChallenges()
        observeChallengeDeleteState()
        challengesViewModel.getChallenges()
    }

    private fun observeChallenges() {
        challengesViewModel.challenges.observe(viewLifecycleOwner, Observer { challenges ->
            when {
                challenges.error -> Toast.makeText(context, "Unable to retrieve challenge requests", Toast.LENGTH_SHORT).show()
                challenges.data.isNullOrEmpty() -> Toast.makeText(context, "No challenge requests", Toast.LENGTH_SHORT).show()
                else -> setAdapter(challenges.data)
            }
        })
    }

    private fun observeChallengeDeleteState() {
        challengesViewModel.challengeDeleteState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ChallengeDeleteState.SUCCESS -> {
                    if (deletePosition >= 0) {
                        requestedChallengesRecyclerView.removeViewAt(deletePosition)
                        challengesAdapter.deleteChallengeAtPosition(deletePosition)
                        deletePosition = -1
                    }
                }
                ChallengeDeleteState.FAIL -> Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(challenges: ArrayList<Challenge>) {
        challengesAdapter =  RequestedChallengesAdapter(challenges, this)
        val viewManager = LinearLayoutManager(activity)
        requestedChallengesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = challengesAdapter
        }
    }

    override fun onClick(id: String, position: Int) {
        val dialog = ChallengeActionDialog(this as IChallengeActionListener, id, position)
        dialog.show(childFragmentManager, "AcceptChallengeDialog")
    }

    override fun onDialogPositiveClick(challengeId: String) {
        challengesFragment.acceptChallenge(challengeId)
    }

    override fun onDialogNegativeClick(challengeId: String, position: Int) {
        deletePosition = position
        challengesViewModel.deleteChallenge(challengeId)
    }
}