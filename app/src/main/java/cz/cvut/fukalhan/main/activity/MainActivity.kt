package cz.cvut.fukalhan.main.activity

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.repository.entity.states.SignOutState
import cz.cvut.fukalhan.utils.network.NetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.menuHeader

class MainActivity : AppCompatActivity(), ILoginNavigation {

    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var networkReceiver: NetworkReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            logOut()
        }
        setContentView(R.layout.activity_main)
        mainActivityViewModel = MainActivityViewModel()
        //setSupportActionBar(toolbar_main)
        observeSignOutState()
        setSideMenuView()
    }

    /** Observe if sign out was called, log out if was */
    private fun observeSignOutState() {
        mainActivityViewModel.signOutState.observe(this, Observer { signOutState ->
            when(signOutState) {
                SignOutState.SUCCESS -> logOut()
                SignOutState.FAIL -> Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Configure app bar to change according current top level destination,
     * set menu navigation view
     */
    private fun setSideMenuView() {
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

        // Sign out user if menu sign out button is clicked
        /*nav_view.sign_out_button.setOnClickListener {
            mainActivityViewModel.signOutUser()
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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

    /** Navigate to login activity */
    override fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
