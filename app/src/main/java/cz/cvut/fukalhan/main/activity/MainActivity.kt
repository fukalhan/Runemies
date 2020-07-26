package cz.cvut.fukalhan.main.activity

import android.app.Activity
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILocationTracking
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.service.LocationService
import cz.cvut.fukalhan.service.LocationServiceBinder
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.network.NetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Application main screen,
 * implement navigation between individual sections of application (fragments) and
 * bound location tracking service to application
 */
class MainActivity : AppCompatActivity(), ILoginNavigation, ILocationTracking {
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var networkReceiver: NetworkReceiver = NetworkReceiver()
    private var service: LocationService? = null
    private var bound = false
    private lateinit var serviceConnection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) logOut()
        setContentView(R.layout.activity_main)
        connectLocationService()
        setSupportActionBar(toolbar_main)
        setBottomMenuView()
        checkPermissions()
    }

    private fun connectLocationService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
                val binder = iBinder as (LocationServiceBinder)
                service = binder.service
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                service = null
                bound = false
            }
        }
    }

    /**
     * Configure app bar to change according current top level destination,
     * set menu navigation view
     */
    private fun setBottomMenuView() {
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_run,
                R.id.nav_profile,
                R.id.nav_activity,
                R.id.nav_challenges,
                R.id.nav_enemies))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    /** Handles back navigation */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Request for permissions,
     * if permissions are granted => bind location tracking service
     */
    private fun checkPermissions() {
        Dexter.withActivity(this)
            .withPermissions(Constants.permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    val trackingIntent = Intent(this@MainActivity, LocationService::class.java)
                    bindService(trackingIntent, serviceConnection, Context.BIND_AUTO_CREATE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {}
            }).check()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(networkReceiver, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(networkReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(serviceConnection)
            bound = false
        }
        super.onDestroy()
    }

    /** Navigate to login activity */
    override fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /** Start tracking service */
    override fun startTracking() {
        service?.startLocationTracking()
    }

    override fun pauseTracking() {
        service?.pauseLocationTracking()
    }

    override fun continueTracking() {
        service?.continueLocationTracking()
    }

    /** Stop tracking service */
    override fun stopTracking() {
        service?.stopLocationTracking()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                Log.e("MainActivity", "request gps")
            }
        }
    }
}
