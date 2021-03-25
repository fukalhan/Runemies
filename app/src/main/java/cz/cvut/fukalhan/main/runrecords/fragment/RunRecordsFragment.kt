package cz.cvut.fukalhan.main.runrecords.fragment

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

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.runrecords.adapter.RunRecordsAdapter
import cz.cvut.fukalhan.main.runrecords.dialog.DeleteRecordDialog
import cz.cvut.fukalhan.main.runrecords.dialog.IDeleteRecordListener
import cz.cvut.fukalhan.main.runrecords.viewModel.RunRecordsViewModel
import cz.cvut.fukalhan.main.runrecords.viewholder.IDeleteButtonListener
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordDeleteState
import kotlinx.android.synthetic.main.fragment_run_records.*

/**
 * Display user's run records
 */
class RunRecordsFragment : Fragment(), IDeleteRecordListener, IDeleteButtonListener {
    private lateinit var runRecordsViewModel: RunRecordsViewModel
    private lateinit var recordsAdapter: RunRecordsAdapter
    private var deletePosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        runRecordsViewModel = ViewModelProvider(requireActivity()).get(RunRecordsViewModel::class.java)
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
                records.data.isNullOrEmpty() -> Toast.makeText(context, "No run records available", Toast.LENGTH_SHORT).show()
                else -> setAdapter(records.data)
            }
        })
    }

    private fun setAdapter(userActivities: ArrayList<RunRecord>) {
        recordsAdapter = RunRecordsAdapter(userActivities, this)
        val viewManager = LinearLayoutManager(activity)
        user_activity_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recordsAdapter
        }
    }

    override fun onClick(recordId: String, position: Int) {
        val dialog = DeleteRecordDialog(this as IDeleteRecordListener, recordId, position)
        dialog.show(childFragmentManager, "DeleteRecordDialog")
    }

    private fun observeDeleteRecordState() {
        runRecordsViewModel.recordDeleteState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                RecordDeleteState.SUCCESS -> {
                    if (deletePosition >= 0) {
                        user_activity_recycler_view.removeViewAt(deletePosition)
                        recordsAdapter.deleteRecordOnPosition(deletePosition)
                        deletePosition = -1
                    }
                }
                RecordDeleteState.FAIL -> Toast.makeText(context, "Unable to delete record", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDialogPositiveClick(recordId: String, position: Int) {
        deletePosition = position
        runRecordsViewModel.deleteRecord(recordId)
    }

    override fun onDialogNegativeClick() {
    }
}
