package cz.cvut.fukalhan.main.enemies.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ChallengeUserDialog(private val listener: IChallengeUserListener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Challenge user?")
                .setPositiveButton("Yes") { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton("No") { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}