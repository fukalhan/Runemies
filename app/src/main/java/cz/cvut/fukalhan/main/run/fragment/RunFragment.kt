package cz.cvut.fukalhan.main.run.fragment

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
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.IOnGpsListener
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.shared.LocationTrackingRecord
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import cz.cvut.fukalhan.utils.gps.GpsUtil
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.run_buttons.*
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback, KoinComponent, IOnGpsListener {
    private lateinit var viewModel: RunViewModel
    private val locationTrackingRecord by inject<LocationTrackingRecord>()
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var location: LatLng
    private var marker: Marker? = null
    private lateinit var markerOptions: MarkerOptions
    private var polyline: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_run, container, false)
        viewModel = RunViewModel(viewLifecycleOwner)
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
    }

    /** Set actions to buttons and set observer on run record saving state */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGpsStateListener()
        setButtonListeners()
        observeSavingRunRecord()
    }

    /** */
    private fun setGpsStateListener() {
        GpsUtil.enabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            when (isEnabled) {
                true -> {
                    gps_flag.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    gps_check.visibility = View.VISIBLE
                }
                false -> {
                    gps_flag.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    gps_check.visibility = View.GONE
                    GpsUtil.turnGpsOn(this)
                }
            }
        })
    }

    /** Set functionality of the buttons controlling the start and end of location tracking*/
    private fun setButtonListeners() {
        setStartButtonAction()

        pause_button.setOnClickListener {
            pause_button.visibility = View.GONE
            continue_button.visibility = View.VISIBLE
        }

        continue_button.setOnClickListener {
            pause_button.visibility = View.VISIBLE
            continue_button.visibility = View.GONE
        }

        setEndButtonAction()
    }

    /**
     * Set action on start click
     * - start location tracking service
     * - hide bottom navigation bar so user can't go to other parts of app while tracking location
     * to not mess with map view (which is really messy) - workaround, maybe improve later
     * - observe for new location update from location tracking record
     * - show and hide buttons according to determined behaviour
     */
    private fun setStartButtonAction() {
        start_button.setOnClickListener {
            // Start requesting location updates
            (activity as ILocationTracking).startTracking()
            // While location tracking is running the bottom navigation view is hidden
            showBottomNavBar(false)
            locationTrackingRecord.locationChanged.observe(viewLifecycleOwner, Observer { locationChanged ->
                // Remove previous marker, set marker on a new location and move camera on that location
                marker?.remove()
                location = locationChanged.pathWay.points.last()
                Toast.makeText(context, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
                markerOptions.position(location)
                marker = map.addMarker(markerOptions)
                map.animateCamera(CameraUpdateFactory.newLatLng(location))

                // Redraw the polyline according to new data
                polyline?.remove()
                polyline = map.addPolyline(locationChanged.pathWay)

                // Update distance count and pace
                distance.text = String.format("%.2f", locationChanged.distance)
                tempo.text = TimeFormatter.toMinSec(locationChanged.currentPace)
            })

            start_button.visibility = View.GONE
            end_button.visibility = View.VISIBLE
            pause_button.visibility = View.VISIBLE
        }
    }

    private fun setEndButtonAction() {
        end_button.setOnClickListener {
            // Stop requesting location updates
            (activity as ILocationTracking).stopTracking()

            userAuth?.let {
                viewModel.saveRunRecord(userAuth.uid)
            }
            // When location tracking stops, the bottom navigation view is visible again
            showBottomNavBar(true)

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
                    resetRunData()
                }
                RunRecordSaveState.FAIL -> Toast.makeText(context, "Run record wasn't saved", Toast.LENGTH_SHORT).show()
                RunRecordSaveState.CANNOT_ADD_RECORD -> TODO()
                RunRecordSaveState.CANNOT_UPDATE_STATISTICS -> TODO()
                RunRecordSaveState.NOT_EXISTING_USER -> TODO()
            }
        })
    }

    private fun resetRunData() {
        polyline?.remove()
        marker?.remove()
        // Set marker on last known position
        marker = map.addMarker(markerOptions.position(LatLng(location.latitude, location.longitude)))
        distance.text = getString(R.string.distance_reset)
        timer.text = getString(R.string.timer_reset)
        tempo.text = getString(R.string.tempo_reset)
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
            location = LatLng(lastLocation.latitude, lastLocation.longitude)
            // Move camera on given location and set marker there
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            markerOptions.position(location)
            marker = map.addMarker(markerOptions)
        }
    }

    override fun onStart() {
        super.onStart()
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
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("Run fragment", "is destroyed")
        map.clear()
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

    /** Change GPS flag view according to if GPS is turned on or off */
    override fun gpsStatus(isGpsEnabled: Boolean) {
        if (isGpsEnabled) {
            gps_flag.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            gps_check.visibility = View.VISIBLE
        } else {
            gps_flag.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            gps_check.visibility = View.GONE
        }
    }
}
