package cz.cvut.fukalhan.main.run.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.IOnGpsListener
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import cz.cvut.fukalhan.utils.GpsUtil
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.run_buttons.*
import org.koin.core.KoinComponent

/**
 * Run recording screen,
 * show current run statistics, map view and buttons handling run recording
 */
class RunFragment : Fragment(), OnMapReadyCallback, KoinComponent, IOnGpsListener {
    private lateinit var runViewModel: RunViewModel
    private var recording: Boolean = false
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var location: LatLng
    private var marker: Marker? = null
    private lateinit var markerOptions: MarkerOptions
    private var polyline: Polyline? = null
    private lateinit var polylineOptions: PolylineOptions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_run, container, false)
        runViewModel = RunViewModel()
        runViewModel.registerBus()
        (activity as ILocationTracking).startTracking()
        setMapView(savedInstanceState, view)
        customizeMarker()
        return view
    }

    /** Initialize Map View */
    private fun setMapView(savedInstanceState: Bundle?, view: View) {
        var mapViewBundle: Bundle? = null
        savedInstanceState?.let { mapViewBundle = it.getBundle(Constants.MAPVIEW_BUNDLE_KEY) }
        mapView = view.findViewById(R.id.map_view) as MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    /**
     * Initial setting of map once it's available,
     * this callback is triggered once the map is ready to be used
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val lastLocation = (activity as ILocationTracking).getLastLocation()
        lastLocation?.let {
            val newLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
            markerOptions.position(newLocation)
            marker = map.addMarker(markerOptions)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15f))
        }
        runViewModel.runRecord.observe(viewLifecycleOwner, Observer { runRecord ->
            updateLocation(runRecord)
        })
    }

    private fun updateLocation(runRecord: RunRecord) {
        val newLocation = runRecord.pathWay.last()
            marker?.remove()
            markerOptions.position(newLocation)
            marker = map.addMarker(markerOptions)
            map.animateCamera(CameraUpdateFactory.newLatLng(newLocation))
            Toast.makeText(context, "${newLocation.latitude}, ${newLocation.longitude}", Toast.LENGTH_SHORT).show()

            if (recording) {
                // Redraw the polyline according to new data
                polyline?.remove()
                polylineOptions = PolylineOptions().color(R.color.green).addAll(runRecord.pathWay)
                polyline = map.addPolyline(polylineOptions)

                // Update distance count and pace
                distance.text = String.format("%.2f", runRecord.distance)
                tempo.text = TimeFormatter.toMinSec(runRecord.pace)
            }
    }

    /** Design map marker*/
    private fun customizeMarker() {
        val icon = DrawableToBitmapUtil.generateBitmapDescriptor(requireContext(), R.drawable.ic_map_marker)
        markerOptions = MarkerOptions().icon(icon)
    }

    /** Set actions to buttons and set observer on run record saving state */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonAction()
        observeSaveRunRecord()
    }

    /** Set functionality of the buttons controlling the start and end of location tracking*/
    private fun setButtonAction() {
        start_button.setOnClickListener {
            runStarted()
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
            runEnded()
        }
    }

    private fun runStarted() {
        recording = true
        runViewModel.startRecord()
        GpsUtil.turnGpsOn(this, requireContext())
        showBottomNavBar(false)

        start_button.visibility = View.GONE
        end_button.visibility = View.VISIBLE
        pause_button.visibility = View.VISIBLE
    }

    private fun runEnded() {
        recording = false
        showBottomNavBar(true)

        end_button.visibility = View.GONE
        pause_button.visibility = View.GONE
        continue_button.visibility = View.GONE
        start_button.visibility = View.VISIBLE
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
    private fun observeSaveRunRecord() {
        runViewModel.recordSaveState.observe(viewLifecycleOwner, Observer { runRecordState ->
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
        runViewModel.unregisterBus()
        (activity as ILocationTracking).stopTracking()
        map.clear()
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        mapViewBundle?.let {
            mapViewBundle = Bundle()
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun gpsStatus(isGpsEnabled: Boolean) {
    }
}
