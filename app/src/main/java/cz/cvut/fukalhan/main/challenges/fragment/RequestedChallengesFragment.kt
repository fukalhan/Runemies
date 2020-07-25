package cz.cvut.fukalhan.main.challenges.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.challenges.adapter.RequestedChallengesAdapter
import cz.cvut.fukalhan.main.challenges.dialog.AcceptChallengeDialog
import cz.cvut.fukalhan.main.challenges.dialog.IAcceptChallengeListener
import cz.cvut.fukalhan.main.challenges.viewmodel.RequestedChallengesViewModel
import cz.cvut.fukalhan.repository.entity.Challenge
import kotlinx.android.synthetic.main.fragment_active_challenges.*
import kotlinx.android.synthetic.main.fragment_requested_challenges.*
import kotlinx.android.synthetic.main.profile_user_info.*

class RequestedChallengesFragment(private val challengesFragment: ChallengesFragment) : Fragment(), IAcceptChallengeListener {
    private lateinit var challengesViewModel: RequestedChallengesViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        challengesViewModel = RequestedChallengesViewModel()
        return inflater.inflate(R.layout.fragment_requested_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengesViewModel.challenges.observe(viewLifecycleOwner, Observer { challenges ->
            when {
                challenges.error -> Toast.makeText(context, "Cannot retrieve challenges data", Toast.LENGTH_SHORT).show()
                challenges.data.isNullOrEmpty() -> Toast.makeText(context, "No requested challenges", Toast.LENGTH_SHORT).show()
                else -> setAdapter(challenges.data)
            }
        })

        user?.let { challengesViewModel.getChallenges(it.uid) }
    }

    private fun setAdapter(challenges: List<Challenge>) {
        val challengeAdapter = context?.let { context -> RequestedChallengesAdapter(requireContext(), this, challenges, context.resources) }
        val viewManager = LinearLayoutManager(activity)
        requestedChallengesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = challengeAdapter
        }
    }

    fun acceptChallengeDialog(challengeId: String) {
        val dialog = AcceptChallengeDialog(this as IAcceptChallengeListener, challengeId)
        dialog.show(requireFragmentManager(), "AcceptChallengeDialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, challengeId: String) {
        challengesFragment.acceptChallenge(challengeId)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}