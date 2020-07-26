package cz.cvut.fukalhan.main.useractivity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.useractivity.adapter.UserActivityAdapter
import cz.cvut.fukalhan.main.useractivity.dialog.DeleteRecordDialog
import cz.cvut.fukalhan.main.useractivity.dialog.IDeleteRecordListener
import cz.cvut.fukalhan.main.useractivity.viewModel.UserActivityViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RecordDeleteState
import kotlinx.android.synthetic.main.fragment_activity.*

/**
 * A simple [Fragment] subclass.
 */
class UserActivityFragment : Fragment(), IDeleteRecordListener {
    private lateinit var userActivityViewModel: UserActivityViewModel
    private val userAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var recordAdapter: UserActivityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userActivityViewModel = UserActivityViewModel()
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecords()
    }

    private fun getRecords() {
        userActivityViewModel.activities.observe(viewLifecycleOwner, Observer { activities ->
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

        userActivityViewModel.getRecords()
    }

    // Init userActivitiesAdapter in fragment layout's recycler view
    private fun setAdapter(userActivities: ArrayList<RunRecord>) {
        recordAdapter = context?.let { context -> UserActivityAdapter(userActivities, context.resources, this) }
        val viewManager = LinearLayoutManager(activity)
        user_activity_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recordAdapter
        }
    }

    fun makeDeleteRecordDialog(recordId: String, position: Int) {
        val dialog = DeleteRecordDialog(this as IDeleteRecordListener, recordId, position)
        dialog.show(requireFragmentManager(), "DeleteRecordDialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, recordId: String, position: Int) {
        userActivityViewModel.recordDeleteState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                RecordDeleteState.FAIL -> Toast.makeText(context, "Something went wrong, unable to delete record", Toast.LENGTH_SHORT).show()
                RecordDeleteState.SUCCESS -> {
                    recordAdapter?.deleteRecordOnPosition(position)
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })
        userActivityViewModel.deleteRecord(recordId)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}
