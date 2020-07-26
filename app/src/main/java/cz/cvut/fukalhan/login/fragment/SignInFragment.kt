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
import cz.cvut.fukalhan.login.viewmodel.SignInViewModel
import cz.cvut.fukalhan.repository.login.states.PasswordChangeEmailSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        signInViewModel = SignInViewModel()
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_button.setOnClickListener {
            signInViewModel.signIn(sign_in_email.text.toString().trim(), sign_in_password.text.toString())
        }
        observeSignInState()
        forgotten_password.setOnClickListener {
            signInViewModel.forgotPassword(sign_in_email.text.toString().trim())
        }
        observeNewPasswordEmailSentState()
    }

    private fun observeSignInState() {
        signInViewModel.signInState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SignInState.SUCCESS -> {
                    Toast.makeText(context, "Sign in", Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).navigateToMainScreen()
                }
                SignInState.NOT_EXISTING_ACCOUNT -> Toast.makeText(context, "Account doesn't exist", Toast.LENGTH_SHORT).show()
                SignInState.WRONG_PASSWORD -> Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                SignInState.FAIL -> Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeNewPasswordEmailSentState() {
        signInViewModel.newPassword.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                PasswordChangeEmailSentState.SUCCESS -> Toast.makeText(context, "Email with new password link was sent to Your email address", Toast.LENGTH_SHORT).show()
                PasswordChangeEmailSentState.NOT_EXISTING_USER -> Toast.makeText(context, "Account doesn't exists", Toast.LENGTH_SHORT).show()
                PasswordChangeEmailSentState.FAIL -> Toast.makeText(context, "Oops, something went wrong, cannot send the email", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
