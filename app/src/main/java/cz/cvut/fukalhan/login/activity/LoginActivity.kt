package cz.cvut.fukalhan.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.adapter.LoginAdapter
import cz.cvut.fukalhan.main.activity.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.IllegalStateException

class LoginActivity : AppCompatActivity(),
    ILoginNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setView()
    }

    private fun setView() {
        loginViewPager.adapter = LoginAdapter(this)
        TabLayoutMediator(loginTabLayout, loginViewPager) { tab, position ->
            tab.text = when(position){
                0 -> "SIGN IN"
                1 -> "SIGN UP"
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    override fun onBackPressed() {
        Log.d("LoginActivity", "Back pressed, not logged in")
    }

    override fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
