package cz.cvut.fukalhan.main.run.fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CustomCap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.TimeFormatter
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.LocationTracking
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.run_buttons.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel: RunViewModel
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var location: Location
    private var marker: Marker? = null
    private lateinit var markerOptions: MarkerOptions
    private var polyline: Polyline? = null
    private lateinit var polylineOptions: PolylineOptions
    private var time: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_run, container, false)
        viewModel = RunViewModel()
        setMapView(savedInstanceState, view)
        customizeMapObjects()
        return view
    }

    /** Initialize mapView */
    private fun setMapView(savedInstanceState: Bundle?, view: View) {
        var mapViewBundle: Bundle? = null
        savedInstanceState?.let { mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY) }
        mapView = view.findViewById(R.id.map_view) as MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    /** Sets icon to map marker and width and end cap to map polyline */
    private fun customizeMapObjects() {
        val icon = DrawableToBitmapUtil.generateBitmapDescriptor(requireContext(), R.drawable.ic_map_marker)
        markerOptions = MarkerOptions().icon(icon)
        polylineOptions = PolylineOptions().color(ContextCompat.getColor(requireContext(), R.color.green)).endCap(CustomCap(icon))
    }

    /** Set actions to buttons and set observer on run record saving state */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonListeners()
        observeSavingRunRecord()
    }

    /** Set functionality of the buttons controlling the start and end of location tracking*/
    private fun setButtonListeners() {
        start_button.setOnClickListener {
            (activity as ILocationTracking).startTracking()
            // While location tracking is running the bottom navigation view is hidden
            showBottomNavBar(false)
            // Start requesting location updates
            time = System.currentTimeMillis()

            start_button.visibility = View.GONE
            end_button.visibility = View.VISIBLE
            pause_button.visibility = View.VISIBLE
        }

        pause_button.setOnClickListener {
            pause_button.visibility = View.GONE
            continue_button.visibility = View.VISIBLE
        }

        continue_button.setOnClickListener {
            pause_button.visibility = View.VISIBLE
            continue_button.visibility = View.GONE
        }

        end_button.setOnClickListener {
            // Stop requesting location updates
            (activity as ILocationTracking).stopTracking()
            userAuth?.let {
                viewModel.saveRunRecord(userAuth.uid, LocationTracking.d)
            }
            // When location tracking stops, the bottom navigation view is visible again
            showBottomNavBar(true)
            polyline?.remove()
            // Set marker on last known position
            marker = map.addMarker(markerOptions.position(LatLng(location.latitude, location.longitude)))

            end_button.visibility = View.GONE
            pause_button.visibility = View.GONE
            continue_button.visibility = View.GONE
            start_button.visibility = View.VISIBLE
        }
    }

    /** Determines if bottom navigation should be visible*/
    private fun showBottomNavBar(visible: Boolean) {
        val bottomNavBar = activity?.findViewById(R.id.nav_view) as BottomNavigationView
        if (visible) {
            bottomNavBar.visibility = View.VISIBLE
        } else {
            bottomNavBar.visibility = View.GONE
        }
    }

    /** Set observer on state of saving run record */
    private fun observeSavingRunRecord() {
        viewModel.runRecordState.observe(viewLifecycleOwner, Observer { runRecordState ->
            when (runRecordState) {
                RunRecordSaveState.SUCCESS -> {

                    Toast.makeText(context, "Run record saved", Toast.LENGTH_SHORT).show()
                }
                RunRecordSaveState.FAIL -> Toast.makeText(context, "Run record wasn't saved", Toast.LENGTH_SHORT).show()
                RunRecordSaveState.CANNOT_ADD_RECORD -> TODO()
                RunRecordSaveState.CANNOT_UPDATE_STATISTICS -> TODO()
                RunRecordSaveState.NOT_EXISTING_USER -> TODO()
            }
        })
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListenLocation(event: LocationChanged?) {
        event?.let {
            location = event.location
            marker?.remove()
            polyline?.remove()
            val coordinates = LatLng(location.latitude, location.longitude)

            // Show coordinates in Toast
            val text = "${location.latitude}, ${location.longitude}"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

            // Display distance and tempo
            distance.text = String.format("%.2f", event.distance)
            tempo.text = TimeFormatter.toMinSec(event.tempo)
            // Move camera to given coordinates
            map.animateCamera(CameraUpdateFactory.newLatLng(coordinates))
            // Draw polyline
            polyline = map.addPolyline(LocationTracking.pathPoints)
        }
    }

    /**
     * Initial setting of map once it's available,
     * this callback is triggered once the map is ready to be used
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Retrieve last known location from location tracking service
        val lastLocation = (activity as ILocationTracking).getLastLocation()
        lastLocation?.let {
            location = lastLocation
            val coordinates = LatLng(location.latitude, location.longitude)
            // Move camera on given coordinates and set marker there
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
            markerOptions.position(coordinates)
            marker = map.addMarker(markerOptions)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        Log.e("Run fragment", "is paused")
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        Log.e("Run fragment", "is stopped")
        mapView.onStop()
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("Run fragment", "is destroyed")
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
