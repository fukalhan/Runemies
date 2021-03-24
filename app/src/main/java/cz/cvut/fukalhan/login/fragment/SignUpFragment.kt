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
import cz.cvut.fukalhan.login.viewmodel.SignUpViewModel
import cz.cvut.fukalhan.repository.login.states.SignUpState
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        signUpViewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_up_button.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            when {
                sign_up_password.text.toString() != confirmPasswordSignUp.text.toString() ->
                    Toast.makeText(context, getString(R.string.passwords_not_same), Toast.LENGTH_SHORT).show()
                inputEmpty() ->
                    Toast.makeText(context, getString(R.string.field_empty), Toast.LENGTH_SHORT).show()
                else ->
                    signUpViewModel.signUp(sign_up_username.text.toString().trim(), sign_up_email.text.toString().trim(), sign_up_password.text.toString().trim())
            }
        }
        observeSignUpState()
    }

    private fun inputEmpty(): Boolean {
        return when {
            sign_up_username.text.toString().trim() == "" -> true
            sign_up_email.text.toString().trim() == "" -> true
            sign_up_password.text.toString().trim() == "" -> true
            else -> false
        }
    }

    private fun observeSignUpState() {
        signUpViewModel.signUpState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SignUpState.SUCCESS -> {
                    Toast.makeText(context, getString(R.string.sign_up), Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).navigateToMainScreen()
                }
                SignUpState.WEAK_PASSWORD -> Toast.makeText(context, getString(R.string.short_password), Toast.LENGTH_SHORT).show()
                SignUpState.EMAIL_ALREADY_REGISTERED -> Toast.makeText(context, getString(R.string.email_already_registered), Toast.LENGTH_SHORT).show()
                SignUpState.INVALID_EMAIL -> Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                SignUpState.FAIL -> Toast.makeText(context, getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show()
            }
            progress_bar.visibility = View.GONE
        })
    }
}
