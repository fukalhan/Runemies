package cz.cvut.fukalhan.main.challenges.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AcceptChallengeDialog(private val listener: IAcceptChallengeListener, private val challengeId: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Accept the challenge and start the run?")
                .setPositiveButton("Yes") { _, _ ->
                    listener.onDialogPositiveClick(this, challengeId)
                }
                .setNegativeButton("No") { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}