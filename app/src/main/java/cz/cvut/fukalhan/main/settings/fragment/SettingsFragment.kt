package cz.cvut.fukalhan.main.settings.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.settings.viewmodel.SettingsViewModel
import cz.cvut.fukalhan.shared.Constants
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    private fun hideBottomNav() {
        val bottomNavBar = activity?.findViewById(R.id.nav_view) as BottomNavigationView
        bottomNavBar.visibility = View.GONE
    }

    private fun selectImage() {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(intent, "Select Image from here..."), Constants.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE && data != null) {
                user?.let { settingsViewModel.setProfileImage(it, data.data!!) }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

        /*if (requestCode == Constants.PICK_IMAGE_REQUEST && data != null) {
            try {
                user?.let { settingsViewModel.setProfileImage(it, data.data!!) }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }*/
    }
}
