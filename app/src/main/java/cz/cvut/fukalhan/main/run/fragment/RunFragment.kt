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
import com.google.firebase.ktx.Firebase

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.run.viewmodel.RunViewModel
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.repository.useractivity.RunRecordState
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_run.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback {

    private lateinit var runViewModel: RunViewModel
    private val userAuth = FirebaseAuth.getInstance().currentUser
    lateinit var map: GoogleMap
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        runViewModel = RunViewModel()
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_run_fab.setOnClickListener {
            if (userAuth != null) {
                runViewModel.saveRunRecord(userAuth.uid, Calendar.getInstance().timeInMillis, 5.6, 1800000, 10000)
            } else {
                Toast.makeText(context, "No currently signed user", Toast.LENGTH_SHORT).show()
            }
        }

        runViewModel.runRecordState.observe(viewLifecycleOwner, Observer { runRecordState ->
            when(runRecordState) {
                RunRecordState.SUCCESS -> Toast.makeText(context, "Run record saved", Toast.LENGTH_SHORT).show()
                RunRecordState.FAIL -> Toast.makeText(context, "Run record wasn't saved", Toast.LENGTH_SHORT).show()
            }
        })
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
