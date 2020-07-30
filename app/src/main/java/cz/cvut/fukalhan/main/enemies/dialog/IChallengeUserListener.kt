package cz.cvut.fukalhan.main.enemies.dialog

import androidx.fragment.app.DialogFragment

interface IChallengeUserListener {
    fun onDialogPositiveClick(dialog: DialogFragment, opponentId: String, opponentUsername: String)
    fun onDialogNegativeClick(dialog: DialogFragment)
}