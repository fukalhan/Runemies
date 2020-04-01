package cz.cvut.fukalhan.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isUserLoggedIn() : Boolean {
        // TODO everything
        return false
    }
}
