package cz.cvut.fukalhan.login.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_sign_in.*

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton.setOnClickListener {
            val loginInfo = "Username: " + usernameOrEmailSignIn.text + "\nPassword: " + passwordSignIn.text
            Toast.makeText(context, loginInfo, Toast.LENGTH_SHORT).show()
            (activity as LoginActivity).navigateToMainScreen()
        }
    }

}
