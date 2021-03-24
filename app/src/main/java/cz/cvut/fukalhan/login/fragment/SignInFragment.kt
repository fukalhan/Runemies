package cz.cvut.fukalhan.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.login.viewmodel.SignInViewModel
import cz.cvut.fukalhan.repository.login.states.NewPasswordSentState
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.progress_bar

/**
 * User sign in screen
 */
class SignInFragment : Fragment() {
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        signInViewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            if (inputEmpty()) {
                Toast.makeText(context, getString(R.string.field_empty), Toast.LENGTH_SHORT).show()
            } else {
                signInViewModel.signIn(email.text.toString().trim(), password.text.toString())
            }
        }
        observeSignInState()
        passwordForgotten.setOnClickListener {
            signInViewModel.forgotPassword(email.text.toString().trim())
        }
        observeNewPasswordSentState()
    }

    private fun inputEmpty(): Boolean {
        return when {
            email.text.toString().trim() == "" -> true
            password.text.toString().trim() == "" -> true
            else -> false
        }
    }

    private fun observeSignInState() {
        signInViewModel.signInState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SignInState.SUCCESS -> {
                    Toast.makeText(context, getString(R.string.sign_in), Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).navigateToMainScreen()
                }
                SignInState.NOT_EXISTING_ACCOUNT -> Toast.makeText(context, getString(R.string.account_doesnt_exist), Toast.LENGTH_SHORT).show()
                SignInState.WRONG_PASSWORD -> Toast.makeText(context, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
                SignInState.FAIL -> Toast.makeText(context, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show()
            }
            progress_bar.visibility = View.VISIBLE
        })
    }

    private fun observeNewPasswordSentState() {
        signInViewModel.newNewPasswordSentState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                NewPasswordSentState.SUCCESS -> Toast.makeText(context, getString(R.string.new_password_email), Toast.LENGTH_SHORT).show()
                NewPasswordSentState.NOT_EXISTING_ACCOUNT -> Toast.makeText(context, getString(R.string.account_doesnt_exist), Toast.LENGTH_SHORT).show()
                NewPasswordSentState.FAIL -> Toast.makeText(context, getString(R.string.cannot_send_email), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
