package cz.cvut.fukalhan.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.adapter.LoginAdapter
import cz.cvut.fukalhan.main.activity.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.IllegalStateException

/**
 * Application login screen,
 * implement navigation between sign in and sign up screens,
 * and navigation to main screen if user is successfully logged in
 */
class LoginActivity : AppCompatActivity(), ILoginNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTabNavigation()
    }

    /** Set tabs for sign in and sign up screen and enable navigation between them */
    private fun setTabNavigation() {
        loginViewPager.adapter = LoginAdapter(this)
        TabLayoutMediator(loginTabLayout, loginViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "SIGN IN"
                1 -> "SIGN UP"
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    /** On login screen if back pressed without sign in or sign up do nothing */
    override fun onBackPressed() {}

    override fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
