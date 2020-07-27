package cz.cvut.fukalhan.main.runrecords.fragment

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
import cz.cvut.fukalhan.main.runrecords.adapter.RunRecordsAdapter
import cz.cvut.fukalhan.main.runrecords.dialog.DeleteRecordDialog
import cz.cvut.fukalhan.main.runrecords.dialog.IDeleteRecordListener
import cz.cvut.fukalhan.main.runrecords.viewModel.RunRecordsViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordDeleteState
import kotlinx.android.synthetic.main.fragment_run_records.*

/**
 * A simple [Fragment] subclass.
 */
class RunRecordsFragment : Fragment(), IDeleteRecordListener {
    private lateinit var runRecordsViewModel: RunRecordsViewModel
    private var recordAdapterRun: RunRecordsAdapter? = null
    private var position: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        runRecordsViewModel = RunRecordsViewModel()
        return inflater.inflate(R.layout.fragment_run_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeRecords()
        observeDeleteRecordState()
        runRecordsViewModel.getRecords()
    }

    private fun observeRecords() {
        runRecordsViewModel.runRecords.observe(viewLifecycleOwner, Observer { records ->
            when {
                records.error -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                else -> setAdapter(records.data!!)
            }
        })
    }

    private fun setAdapter(userActivities: ArrayList<RunRecord>) {
        recordAdapterRun = context?.let { context -> RunRecordsAdapter(userActivities, context.resources, this) }
        val viewManager = LinearLayoutManager(activity)
        user_activity_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recordAdapterRun
        }
        observeRecordsCount()
    }

    private fun observeRecordsCount() {
        recordAdapterRun?.recordsCount?.observe(viewLifecycleOwner, Observer { count ->
            when (count) {
                0 -> activity_state.text = getString(R.string.no_activity_records)
                1 -> activity_state.text = getString(R.string.one_record, count)
                else -> activity_state.text = getString(R.string.records_count, count)
            }
        })
    }

    private fun observeDeleteRecordState() {
        runRecordsViewModel.recordDeleteState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                RecordDeleteState.SUCCESS -> {
                    if (position >= 0) {
                        user_activity_recycler_view.removeViewAt(position)
                        recordAdapterRun?.deleteRecordOnPosition(position)
                        position = -1
                    }
                }
                RecordDeleteState.FAIL -> Toast.makeText(context, "Unable to delete record", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun makeDeleteRecordDialog(recordId: String, position: Int) {
        val dialog = DeleteRecordDialog(this as IDeleteRecordListener, recordId, position)
        dialog.show(requireFragmentManager(), "DeleteRecordDialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, recordId: String, deletePosition: Int) {
        position = deletePosition
        runRecordsViewModel.deleteRecord(recordId)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}
