package cz.cvut.fukalhan.main.challenges.dialog

import androidx.fragment.app.DialogFragment

interface IChallengeActionListener {
    fun onDialogPositiveClick(challengeId: String)
    fun onDialogNegativeClick(challengeId: String, position: Int)
}