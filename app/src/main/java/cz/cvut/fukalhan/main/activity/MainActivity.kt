package cz.cvut.fukalhan.main.activity

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
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
        val navigationController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_run,
                R.id.nav_profile,
                R.id.nav_activity,
                R.id.nav_challenges,
                R.id.nav_enemies))
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        nav_view.setupWithNavController(navigationController)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Handles back navigation */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(networkReceiver, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(networkReceiver)
        // If service is bound to the Activity, unbind it
        if (bound) {
            unbindService(serviceConnection)
            bound = false
        }
        super.onStop()
    }

    /** Navigate to login activity */
    override fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /** Start tracking service */
    override fun startTracking() {
        service?.startLocationTracking(applicationContext)
    }

    /** Stop tracking service */
    override fun stopTracking() {
        service?.stopLocationTracking()
    }
}
