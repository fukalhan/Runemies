package cz.cvut.fukalhan.main.settings.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.settings.viewmodel.SettingsViewModel
import cz.cvut.fukalhan.shared.Constants
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), ISetUsernameListener {
    private lateinit var settingsViewModel: SettingsViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val storage = Firebase.storage
    private val storageRef: StorageReference = storage.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsViewModel = SettingsViewModel()
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNav()
        change_profile_pic.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .start()
        }

        change_username.setOnClickListener {
            val dialog = SetUsernameDialog(this as ISetUsernameListener)
            dialog.show(requireFragmentManager(), "SetUsernameDialog")
        }
    }

    private fun hideBottomNav() {
        val bottomNavBar = activity?.findViewById(R.id.nav_view) as BottomNavigationView
        bottomNavBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE && data != null) {
                user?.let {
                    val uri = data.data!!
                    val profileImageRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${it.uid}")
                    settingsViewModel.setProfileImage(uri, profileImageRef)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUsernameSaveClick(dialog: DialogFragment, newUsername: String) {
        user?.let { settingsViewModel.setUsername(user, newUsername) }
    }
}
