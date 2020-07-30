package cz.cvut.fukalhan.main.run.dialog

import androidx.fragment.app.DialogFragment

interface ISaveDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment)
    fun onDialogNegativeClick(dialog: DialogFragment)
}