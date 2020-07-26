package cz.cvut.fukalhan.main.challenges.dialog

import androidx.fragment.app.DialogFragment

interface IChallengeActionListener {
    fun onDialogPositiveClick(dialog: DialogFragment, challengeId: String)
    fun onDialogNegativeClick(dialog: DialogFragment, challengeId: String, position: Int)
}