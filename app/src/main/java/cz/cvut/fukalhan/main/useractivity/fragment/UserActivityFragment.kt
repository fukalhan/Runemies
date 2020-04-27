package cz.cvut.fukalhan.main.useractivity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.useractivity.adapter.UserActivityAdapter
import cz.cvut.fukalhan.main.useractivity.viewModel.UserActivityViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import kotlinx.android.synthetic.main.fragment_activity.*

/**
 * A simple [Fragment] subclass.
 */
class UserActivityFragment : Fragment() {

    lateinit var userActivityViewModel: UserActivityViewModel
    val userAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userActivityViewModel = UserActivityViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userActivityViewModel.userActivity.observe(viewLifecycleOwner, Observer { activities ->
            if (activities.isEmpty()) {
                activity_state.text = getString(R.string.no_records)
            } else {
                activity_state.text = getString(R.string.records_count, activities.size)
                setAdapter(activities)
            }
        })

        if (userAuth != null) {
            userActivityViewModel.getUserActivities(userAuth.uid)
        }
    }

    private fun setAdapter(userActivities: List<RunRecord>) {
        val userActivityAdapter = context?.let { UserActivityAdapter(userActivities, it) }
        val viewManager = LinearLayoutManager(activity)
        user_activity_recycler_view.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = userActivityAdapter
        }
    }

}
