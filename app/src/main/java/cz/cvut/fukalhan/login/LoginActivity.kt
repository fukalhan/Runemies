package cz.cvut.fukalhan.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cz.cvut.fukalhan.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setView()
    }

    private fun setView() {
        loginButton.setOnClickListener {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this, "Logged in", duration)
            toast.show()
        }
    }
}
