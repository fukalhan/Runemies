package cz.cvut.fukalhan.main.activity

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.shared.Settings.username
import cz.cvut.fukalhan.utils.network.NetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.menuHeader

class MainActivity : AppCompatActivity(), ILoginNavigation {

    lateinit var user: FirebaseUser
    fun isUserInitialised() = ::user.isInitialized
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var networkReceiver: NetworkReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        setSupportActionBar(toolbar_main)
        setUser()
        setMenuView()
    }

    private fun setMenuView() {
        val navigationController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile,
                R.id.nav_activity,
                R.id.nav_challenges,
                R.id.nav_enemies),
            drawer_layout)
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        nav_view.setupWithNavController(navigationController)
        val headerView = nav_view.getHeaderView(0)
        headerView.menuUsername.text = username

        headerView.menuHeader.setOnClickListener {
            navigationController.navigate(R.id.nav_profile)
            drawer_layout.closeDrawers()
        }
    }

    private fun setUser() {
        mainActivityViewModel.user.observe(this, Observer { firebaseUser ->
            if (firebaseUser == null) {
                logOut()
            } else {
                user = firebaseUser
            }
        })
        mainActivityViewModel.getUser()

        if (isUserInitialised()) {
            username = user.displayName
        }
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
