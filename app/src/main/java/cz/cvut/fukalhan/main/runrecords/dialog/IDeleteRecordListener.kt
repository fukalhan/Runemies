package cz.cvut.fukalhan.main.runrecords.dialog


interface IDeleteRecordListener {
    fun onDialogPositiveClick(recordId: String, position: Int)
    fun onDialogNegativeClick()
}