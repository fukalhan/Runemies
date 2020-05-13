package cz.cvut.fukalhan.main.settings.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.settings.viewmodel.SettingsViewModel
import cz.cvut.fukalhan.shared.Constants
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var filePath: Uri
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = SettingsViewModel()
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNav()
        change_profile_pic.setOnClickListener {
            selectImage()
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

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // if request code is PICK_IMAGE_REQUEST,
        // then set image in the image view
        if (requestCode == Constants.PICK_IMAGE_REQUEST
            && data != null
            && data.data != null) {
                filePath = data.data!!
            try {
                // Setting image on image view using Bitmap
                lateinit var bitmap: Bitmap
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    bitmap = ImageDecoder.createSource(getContentResolver(), filePath)
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath)
                }
                imageView.setImageBitmap(BitmapFactory.decodeFile(filePath))
            } catch (e: Exception) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }*/
}
