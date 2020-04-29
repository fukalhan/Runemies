package cz.cvut.fukalhan.main.useractivity.fragment

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
import cz.cvut.fukalhan.main.useractivity.adapter.UserActivityAdapter
import cz.cvut.fukalhan.main.useractivity.viewModel.UserActivityViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import kotlinx.android.synthetic.main.fragment_activity.*

/**
 * A simple [Fragment] subclass.
 */
class UserActivityFragment : Fragment() {

    private lateinit var userActivityViewModel: UserActivityViewModel
    private val userAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    // Init fragment view model and inflate fragment layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userActivityViewModel = UserActivityViewModel()
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    // Request current user's activities and observe the result
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivitiesObserver()
        userAuth?.let { userActivityViewModel.getUserActivities(it.uid) }
    }

    /**
     * Observe user activity gotten in view model
     * 3 options:
     * -> exception thrown while trying to obtain activities
     * -> there are no activities for current user
     * -> there are some activities
     */
    private fun setActivitiesObserver() {
        userActivityViewModel.activitiesReceiver.observe(viewLifecycleOwner, Observer { activities ->
            when {
                activities.error -> {
                    activity_state.text = getString(R.string.activities_unavailable)
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                activities.data?.isEmpty()!! -> activity_state.text = getString(R.string.no_activity_records)
                else -> {
                    activity_state.text = getString(R.string.records_count, activities.data.size)
                    setAdapter(activities.data)
                }
            }
        })
    }

    // Init userActivitiesAdapter in fragment layout's recycler view
    private fun setAdapter(userActivities: List<RunRecord>) {
        val userActivityAdapter = context?.let { context -> UserActivityAdapter(userActivities, context.resources) }
        val viewManager = LinearLayoutManager(activity)
        user_activity_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = userActivityAdapter
        }
    }
}
