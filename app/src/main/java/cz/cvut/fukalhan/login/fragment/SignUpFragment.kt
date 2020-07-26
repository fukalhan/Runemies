package cz.cvut.fukalhan.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.login.viewmodel.SignUpViewModel
import cz.cvut.fukalhan.repository.login.states.SignUpState
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        signUpViewModel = SignUpViewModel()
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // On Sign up button pressed send user info to signUp view model
        sign_up_button.setOnClickListener {
            sign_up_progress_bar.visibility = View.VISIBLE
            signUpViewModel.signUp(sign_up_username.text.toString(), sign_up_email.text.toString(), sign_up_password.text.toString())
        }
        // Observe data sent from view model
        observeSignUpState()
    }

    private fun observeSignUpState() {
        signUpViewModel.signUpState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SignUpState.SUCCESS -> {
                    Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).navigateToMainScreen()
                }
                SignUpState.WEAK_PASSWORD -> Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show()
                SignUpState.EMAIL_ALREADY_REGISTERED -> Toast.makeText(context, "Email is already registered", Toast.LENGTH_SHORT).show()
                SignUpState.INVALID_EMAIL -> Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                SignUpState.FAIL -> Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
            sign_up_progress_bar.visibility = View.GONE
        })
    }
}
