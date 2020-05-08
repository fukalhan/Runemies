package cz.cvut.fukalhan.main.run.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.TimeFormatter
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.entity.LocationChanged
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.run_buttons.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.sql.Time
import java.util.*
import kotlin.math.roundToLong

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel: RunViewModel
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var map: GoogleMap
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private var time: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var mapViewBundle: Bundle? = null
        savedInstanceState?.let { mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY) }

        val view = inflater.inflate(R.layout.fragment_run, container, false)
        val mapView = view.findViewById(R.id.map_view) as MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        viewModel = RunViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distance.text = 0.0.toString()
        setButtonListeners()
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

    /** Set functionality of the buttons controling the start and end of location tracking*/
    private fun setButtonListeners() {
        start_button.setOnClickListener {
            // Start requesting location updates
            (activity as ILocationTracking).startTracking()
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
            (activity as MainActivity).stopTracking()
            time = System.currentTimeMillis() - time

            end_button.visibility = View.GONE
            pause_button.visibility = View.GONE
            continue_button.visibility = View.GONE
            start_button.visibility = View.VISIBLE
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListenLocation(event: LocationChanged?) {
        event?.let {
            val text = "${event.location.latitude}, ${event.location.longitude}"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

            distance.text = String.format("%.2f", event.distance)
            tempo.text = TimeFormatter.toMinSec(event.tempo)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListenRunRecord(event: RunRecord?) {
        event?.let {
            val runRecord = event
            runRecord.time = time
            runRecord.tempo = (time / runRecord.distance).roundToLong()
            userAuth?.let {user ->
                viewModel.saveRunRecord(user.uid, runRecord)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        map_view.onPause()
        super.onPause()
    }

    override fun onStop() {
        map_view.onStop()
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        map_view.onSaveInstanceState(mapViewBundle)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }
}
