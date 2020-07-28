package cz.cvut.fukalhan.main.run.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.IOnGpsListener
import cz.cvut.fukalhan.main.run.dialog.ISaveDialogListener
import cz.cvut.fukalhan.main.run.dialog.SaveRecordDialog
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.challenges.state.ChallengeState
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.runrecords.states.RecordSaveState
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.DrawableToBitmapUtil
import cz.cvut.fukalhan.utils.GpsUtil
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.run_buttons.*

/**
 * Run recording screen,
 * show current run statistics, map view and buttons handling run recording
 */
class RunFragment : Fragment(), OnMapReadyCallback, IOnGpsListener, ISaveDialogListener {
    private val args: RunFragmentArgs by navArgs()
    private lateinit var challenge: ChallengeState
    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var runViewModel: RunViewModel
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private var firstLocationSetting: Boolean = true
    private var marker: Marker? = null
    private lateinit var markerOptions: MarkerOptions
    private var polyline: Polyline? = null
    private lateinit var polylineOptions: PolylineOptions
    private val recordObserver: Observer<RunRecord> = Observer { updateRecord(it) }
    private var timeWhenStopped: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_run, container, false)
        runViewModel = RunViewModel(requireContext())
        challenge = args.challengeState
        setMapView(savedInstanceState, view)
        customizeMarker()
        view.setOnKeyListener { v, keyCode, event ->
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                false
            }
            false
        }
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
        runViewModel.location.observe(viewLifecycleOwner, Observer { location ->
            updateLocation(location)
        })
    }

    private fun updateLocation(location: LatLng) {
        marker?.remove()
        markerOptions.position(location)
        marker = map.addMarker(markerOptions)
        if (firstLocationSetting) {
            firstLocationSetting = false
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    private fun updateRecord(runRecord: RunRecord) {
        // Redraw the polyline according to new data
        polyline?.remove()
        polylineOptions = PolylineOptions().color(R.color.green).addAll(runRecord.pathWay)
        polyline = map.addPolyline(polylineOptions)

        // Update distance count and pace
        distance.text = String.format("%.2f", runRecord.distance)
        tempo.text = TimeFormatter.toMinSec(runRecord.pace)
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
            runPaused()
        }

        continue_button.setOnClickListener {
            runContinue()
        }

        end_button.setOnClickListener {
            runEnded()
        }
    }

    private fun runStarted() {
        GpsUtil.turnGpsOn(this, requireContext())
        (activity as ILocationTracking).startTracking()
        runViewModel.startRecord()
        runViewModel.record.observeForever(recordObserver)
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        bottomNavBarVisibility(false)

        start_button.visibility = View.GONE
        end_button.visibility = View.VISIBLE
        pause_button.visibility = View.VISIBLE
    }

    private fun runPaused() {
        (activity as ILocationTracking).pauseTracking()
        runViewModel.pauseRecord()
        timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
        timer.stop()
        pause_button.visibility = View.GONE
        continue_button.visibility = View.VISIBLE
    }

    private fun runContinue() {
        (activity as ILocationTracking).continueTracking()
        runViewModel.continueRecord()
        timer.base = SystemClock.elapsedRealtime() + timeWhenStopped
        timer.start()
        pause_button.visibility = View.VISIBLE
        continue_button.visibility = View.GONE
    }

    private fun runEnded() {
        runViewModel.stopRecord()
        runViewModel.record.removeObserver(recordObserver)
        val dialog = SaveRecordDialog(this as ISaveDialogListener)
        dialog.show(requireFragmentManager(), "SaveRecordDialog")
        (activity as ILocationTracking).stopTracking()
        timer.stop()
        timeWhenStopped = 0
        bottomNavBarVisibility(true)

        end_button.visibility = View.GONE
        pause_button.visibility = View.GONE
        continue_button.visibility = View.GONE
        start_button.visibility = View.VISIBLE
    }

    /** Set observer on state of saving run record */
    private fun observeSaveRunRecord() {
        runViewModel.recordSaveResult.observe(viewLifecycleOwner, Observer { recordSaveState ->
            when (recordSaveState) {
                RecordSaveState.SUCCESS -> Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT).show()
                RecordSaveState.FAIL -> Toast.makeText(context, "Run record wasn't saved", Toast.LENGTH_SHORT).show()
            }
            resetRecord()
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
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
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

    /** Determines if bottom navigation should be visible*/
    private fun bottomNavBarVisibility(visible: Boolean) {
        val bottomNavBar = activity?.findViewById(R.id.nav_view) as BottomNavigationView
        if (visible) {
            bottomNavBar.visibility = View.VISIBLE
        } else {
            bottomNavBar.visibility = View.GONE
        }
    }

    private fun resetRecord() {
        polyline?.remove()
        distance.text = getString(R.string.distance_reset)
        timer.base = SystemClock.elapsedRealtime()
        tempo.text = getString(R.string.tempo_reset)
        timer.stop()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        user?.let {
            runViewModel.saveRecord(it.uid)
            when (challenge) {
                ChallengeState.STARTED -> runViewModel.createChallenge(it.uid, it.displayName!!, args.enemyId, args.enemyUsername)
                ChallengeState.ACCEPTED -> runViewModel.updateChallenge(it.uid, args.challengeId)
            }
            challenge = ChallengeState.NULL
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        resetRecord()
    }
}
