package cz.cvut.fukalhan.main.profile.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.main.profile.viewmodel.ProfileViewModel
import cz.cvut.fukalhan.repository.entity.ActivityStatistics
import cz.cvut.fukalhan.repository.entity.User
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.repository.user.states.ImageSet
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.TimeFormatter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_activity_graph.*
import kotlinx.android.synthetic.main.profile_activity_summary.*
import kotlinx.android.synthetic.main.profile_user_info.*

open class ProfileFragment : Fragment() {
    protected lateinit var profileViewModel: ProfileViewModel
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val storageRef: StorageReference = Firebase.storage.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ProfileViewModel()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_out.setOnClickListener {
            profileViewModel.signOutUser()
        }
        image_setting.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .start()
        }
        observeImageChange()
        observeSignOutState()
        setUserData()
        getActivitiesGraph()
    }

    private fun observeImageChange() {
        profileViewModel.imageSetState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ImageSet.FAIL -> Toast.makeText(context, "Sorry, unable to change profile pic, try later.", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(context, "Profile image changed", Toast.LENGTH_SHORT).show()
                    user?.let {
                        setProfileImage(it.uid)
                    }
                }
            }
        })
    }

    private fun observeSignOutState() {
        profileViewModel.signOutState.observe(viewLifecycleOwner, Observer { signOutState ->
            when (signOutState) {
                SignOutState.SUCCESS -> (activity as ILoginNavigation).logOut()
                SignOutState.FAIL -> Toast.makeText(requireContext(), "Sign out failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /** Request data of current user and set observer for the answer */
    protected open fun setUserData() {
        profileViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            when {
                user.error -> Toast.makeText(context, "Cannot retrieve user data", Toast.LENGTH_SHORT).show()
                user.data == null -> Toast.makeText(context, "User doesn't exists", Toast.LENGTH_SHORT).show()
                else -> setUserData(user.data)
            }
        })

        profileViewModel.userStatistics.observe(viewLifecycleOwner, Observer {
            setUserStatistics(it)
        })

        getUserData()
    }

    protected open fun getUserData() {
        user?.let {
            setProfileImage(it.uid)
            username.text = user.displayName
            join_date.text = getString(R.string.joined, TimeFormatter.simpleDate.format(user.metadata?.creationTimestamp))
            profileViewModel.getUserData(it.uid)
            profileViewModel.getUserStatistics(it.uid)
        }
    }

    protected fun setProfileImage(userId: String) {
        val imagePathRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}$userId")
        imagePathRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                Glide.with(requireContext()).load(uri).into(profile_image)
            }
    }

    /** Set data of given user on profile fragment screen */
    protected open fun setUserData(user: User) {
        points.text = user.points.toString()
    }

    private fun setUserStatistics(statistics: ActivityStatistics) {
        total_mileage.text = getString(R.string.total_mileage, String.format("%.2f", statistics.totalMileage))
        total_hours.text = getString(R.string.total_time, TimeFormatter.toHourMinSec(statistics.totalTime))
        longest_run.text = getString(R.string.longest_run, statistics.longestRun.toString())
    }

    private fun getActivitiesGraph() {
        profileViewModel.monthMileage.observe(viewLifecycleOwner, Observer { monthMileage ->
            val entries = ArrayList<BarEntry>()
            for (i in 0..11) {
                entries.add(BarEntry(i.toFloat(), monthMileage[i]))
            }
            val dataSet = BarDataSet(entries, "Km")
            dataSet.setDrawValues(false)
            val data = BarData(dataSet)
            activity_graph.data = data
            activity_graph.axisLeft.axisMinimum = 0f
            activity_graph.axisLeft.isGranularityEnabled = true
            activity_graph.axisLeft.granularity = 10f
            activity_graph.axisLeft.enableGridDashedLine(3f, 3f, 0f)
            activity_graph.axisLeft.setLabelCount(5, true)
            activity_graph.axisRight.isEnabled = false
            activity_graph.xAxis.valueFormatter = ActivityChartFormatter()
            activity_graph.xAxis.setDrawGridLines(false)
            activity_graph.xAxis.position = XAxis.XAxisPosition.BOTTOM
            activity_graph.description.isEnabled = false
            activity_graph.setDrawGridBackground(false)
            activity_graph.setNoDataText("Data unavailable")
            activity_graph.invalidate()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE && data != null) {
                user?.let {
                    val uri = data.data!!
                    val profileImageRef = storageRef.child("${Constants.PROFILE_IMAGE_PATH}${it.uid}")
                    profileViewModel.setProfileImage(uri, profileImageRef)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
