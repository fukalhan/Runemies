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
import cz.cvut.fukalhan.repository.entity.states.SignUpState
import cz.cvut.fukalhan.shared.Settings
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = SignUpViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On Sign up button pressed send user info to signUp view model
        signUpButton.setOnClickListener {
            signUpProgressBar.visibility = View.VISIBLE
            viewModel.signUp( "${usernameSignUp.text}","${emailSignUp.text}", "${passwordSignUp.text}")
        }

        // Observe data sent from view model
        viewModel.signUpState.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                SignUpState.SUCCESS -> {
                    Settings.username = usernameSignUp.text.toString()
                    Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).navigateToMainScreen()
                }
                SignUpState.FAIL -> Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
                SignUpState.WEAK_PASSWORD -> Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show()
            }
            signUpProgressBar.visibility = View.GONE
        })
    }
}
