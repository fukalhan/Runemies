package cz.cvut.fukalhan.main.challenges.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ChallengeActionDialog(private val actionListener: IChallengeActionListener, private val challengeId: String, private val position: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you want to accept this challenge? ")
                .setPositiveButton("Accept") { _, _ ->
                    actionListener.onDialogPositiveClick(this, challengeId)
                }
                .setNegativeButton("Decline") { _, _ ->
                    actionListener.onDialogNegativeClick(this, challengeId, position)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}