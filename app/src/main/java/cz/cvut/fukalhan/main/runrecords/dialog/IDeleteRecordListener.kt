package cz.cvut.fukalhan.main.runrecords.dialog

import androidx.fragment.app.DialogFragment

interface IDeleteRecordListener {
    fun onDialogPositiveClick(dialog: DialogFragment, recordId: String, position: Int)
    fun onDialogNegativeClick(dialog: DialogFragment)
}