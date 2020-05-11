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
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CustomCap
import com.google.firebase.auth.FirebaseAuth

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.TimeFormatter
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import cz.cvut.fukalhan.shared.Constants
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
    private var marker: Marker? = null
    private lateinit var markerOptions: MarkerOptions
    private var polyline: Polyline? = null
    private lateinit var polylineOptions: PolylineOptions
    private var tracking: Boolean = false
    private var firstRequest: Boolean = true
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ILocationTracking).startTracking()
        setButtonListeners()
        observeSavingRunRecord()
    }

    /** Set functionality of the buttons controlling the start and end of location tracking*/
    private fun setButtonListeners() {
        start_button.setOnClickListener {
            (activity as ILocationTracking).resetRecords()
            // Start requesting location updates
            time = System.currentTimeMillis()
            tracking = true
            firstRequest = true

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
            time = System.currentTimeMillis() - time
            tracking = false

            end_button.visibility = View.GONE
            pause_button.visibility = View.GONE
            continue_button.visibility = View.GONE
            start_button.visibility = View.VISIBLE
        }
    }

    /** Set observer on state of saving run record */
    private fun observeSavingRunRecord() {
        viewModel.runRecordState.observe(viewLifecycleOwner, Observer { runRecordState ->
            when (runRecordState) {
                RunRecordSaveState.SUCCESS -> Toast.makeText(context, "Run record saved", Toast.LENGTH_SHORT).show()
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
            marker?.remove()
            polyline?.remove()
            val coordinates = LatLng(event.location.latitude, event.location.longitude)

            // TODO move this logic to onMapReady because we don't want to end up on equador everytime we open run fragment
            if (firstRequest) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
                firstRequest = false
            } else {
                map.animateCamera(CameraUpdateFactory.newLatLng(coordinates))
            }

            // TODO find better solution for showing end cap before the tracking starts so the polyline wouldn't be shown prematurely
            if (!tracking) {
                // Show map marker instead of cap with polyline before the tracking starts
                markerOptions.position(coordinates)
                marker = map.addMarker(markerOptions)
            } else {
                // Show coordinates in Toast
                val text = "${event.location.latitude}, ${event.location.longitude}"
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

                // Display distance and tempo
                distance.text = String.format("%.2f", event.distance)
                tempo.text = TimeFormatter.toMinSec(event.tempo)

                // Draw polyline
                polylineOptions.add(coordinates)
                polylineOptions.visible(true)
                polyline = map.addPolyline(polylineOptions)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
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
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        (activity as ILocationTracking).stopTracking()
        Log.e("Run fragment", "is being destroyed")
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
