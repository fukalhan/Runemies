package cz.cvut.fukalhan.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.LoginNavigation
import cz.cvut.fukalhan.databinding.ActivityLoginBinding
import cz.cvut.fukalhan.login.adapter.LoginAdapter
import cz.cvut.fukalhan.main.MainActivity
import java.lang.IllegalStateException

class LoginActivity : AppCompatActivity(), LoginNavigation {
    private lateinit var binding: ActivityLoginBinding
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user?.let { navigateToMainScreen() }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTabNavigation()
    }

    private fun setTabNavigation() {
        binding.loginViewPager.adapter = LoginAdapter(this)
        TabLayoutMediator(binding.loginTabLayout, binding.loginViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.signIn)
                1 -> getString(R.string.signUp)
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    /** On login screen if back pressed without sign in or sign up, do nothing */
    override fun onBackPressed() {}

    override fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
