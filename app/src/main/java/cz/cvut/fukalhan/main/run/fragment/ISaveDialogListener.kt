package cz.cvut.fukalhan.main.run.fragment

import androidx.fragment.app.DialogFragment

interface ISaveDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment)
    fun onDialogNegativeClick(dialog: DialogFragment)
}