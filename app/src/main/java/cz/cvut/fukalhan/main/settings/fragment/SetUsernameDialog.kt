package cz.cvut.fukalhan.main.settings.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import cz.cvut.fukalhan.R
import kotlinx.android.synthetic.main.dialog_set_username.*

class SetUsernameDialog(private val listener: ISetUsernameListener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setView(layoutInflater.inflate(R.layout.dialog_set_username, null))
                .setMessage("Set new username:")
                .setNeutralButton("Save") { _, _ ->
                    val newUsername = dialog?.findViewById(R.id.new_username) as EditText
                    listener.onUsernameSaveClick(this, newUsername.text.toString())
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}