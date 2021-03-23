package cz.cvut.fukalhan.main.runrecords.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteRecordDialog(private val listener: IDeleteRecordListener, private val recordId: String, private val position: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you wanna delete this record?")
                .setPositiveButton("Yes") { _, _ ->
                    listener.onDialogPositiveClick(recordId, position)
                }
                .setNegativeButton("No") { _, _ ->
                    listener.onDialogNegativeClick()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}