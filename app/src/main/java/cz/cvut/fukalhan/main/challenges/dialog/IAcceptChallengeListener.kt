package cz.cvut.fukalhan.main.challenges.dialog

import androidx.fragment.app.DialogFragment

interface IAcceptChallengeListener {
    fun onDialogPositiveClick(dialog: DialogFragment, challengeId: String)
    fun onDialogNegativeClick(dialog: DialogFragment)
}