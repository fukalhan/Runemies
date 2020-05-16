package cz.cvut.fukalhan.main.activity

import android.app.Activity
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.Context
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import cz.cvut.fukalhan.repository.login.states.SignOutState
import cz.cvut.fukalhan.service.LocationTrackingService
import cz.cvut.fukalhan.service.LocationTrackingServiceBinder
import cz.cvut.fukalhan.shared.Constants
import cz.cvut.fukalhan.utils.network.NetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), ILoginNavigation, ILocationTracking {
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var networkReceiver: NetworkReceiver = NetworkReceiver()
    private var service: LocationTrackingService? = null
    private var bound = false
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
            val binder = iBinder as (LocationTrackingServiceBinder)
            service = binder.service
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            service = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            logOut()
        }
        setContentView(R.layout.activity_main)
        viewModel = MainActivityViewModel()
        setSupportActionBar(toolbar_main)
        observeSignOutState()
        setBottomMenuView()
        checkPermissions()
    }

    /** Observe if sign out was called, log out if was */
    private fun observeSignOutState() {
        viewModel.signOutState.observe(this, Observer { signOutState ->
            when (signOutState) {
                SignOutState.SUCCESS -> logOut()
                SignOutState.FAIL -> Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT).show()
            }
        })
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

    /** Creates options menu in action bar */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    /** Defines navigation for clicking on options menu items */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out -> {
                viewModel.signOutUser()
                return true
            }
            R.id.settings -> {
                navController.navigate(R.id.nav_settings)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Handles back navigation */
    override fun onSupportNavigateUp(): Boolean {
        showBottomNavigationBar()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /** Makes bottom navigation visible */
    private fun showBottomNavigationBar() {
        val view = this.findViewById<View>(android.R.id.content).rootView
        val bottomNavBar = view.findViewById(R.id.nav_view) as BottomNavigationView
        bottomNavBar.visibility = View.VISIBLE
    }

    /** Check permissions and request for them */
    private fun checkPermissions() {
        Dexter.withActivity(this)
            .withPermissions(Constants.permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    val trackingIntent = Intent(this@MainActivity, LocationTrackingService::class.java)
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
        service?.removeNotifications()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(networkReceiver, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(networkReceiver)
        service?.startNotifications()
        super.onStop()
    }

    override fun onDestroy() {
        // If service is bound to the Activity, unbind it
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

    /** Stop tracking service */
    override fun stopTracking() {
        service?.stopLocationTracking()
    }

    override fun getLastLocation(): Location? {
        return service?.getLocation()
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
