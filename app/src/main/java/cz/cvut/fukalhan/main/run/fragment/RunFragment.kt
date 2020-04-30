package cz.cvut.fukalhan.main.run.fragment

import android.os.Bundle
import android.util.Log
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
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.useractivity.states.RunRecordSaveState
import kotlinx.android.synthetic.main.run_buttons.*
import kotlinx.android.synthetic.main.fragment_run.map_view
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: RunViewModel
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private lateinit var map: GoogleMap
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        val view = inflater.inflate(R.layout.fragment_run, container, false)
        val mapView = view.findViewById(R.id.map_view) as MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
        viewModel = RunViewModel()
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun setButtonListeners() {
        start_button.setOnClickListener {
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
            end_button.visibility = View.GONE
            pause_button.visibility = View.GONE
            continue_button.visibility = View.GONE
            start_button.visibility = View.VISIBLE

            userAuth?.let { viewModel.saveRunRecord(userAuth.uid, Calendar.getInstance().timeInMillis, 5.6, 1800000, 10000) }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onStart() {
        super.onStart()
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
        super.onStop()
    }

    override fun onDestroy() {
        if (map_view != null) {
            map_view.onDestroy()
        }
        Log.e("Run fragment", "destroying")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
