package cz.cvut.fukalhan.main.settings.fragment

import androidx.fragment.app.DialogFragment

interface ISetUsernameListener {
    fun onUsernameSaveClick(dialog: DialogFragment, newUsername: String)
}