package cz.cvut.fukalhan.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.viewmodel.SignUpViewModel
import cz.cvut.fukalhan.repository.entity.SignUpState
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
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On Sign up button pressed sends user info to signUp view model
        signUpButton.setOnClickListener {
            signUpProgressBar.visibility = View.VISIBLE
            viewModel.signUp( usernameSignUp.text.toString(),"${emailSignUp.text}", passwordSignUp.text.toString())
        }

        // Observe data sent from view model
        viewModel.signUpState.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                SignUpState.SUCCESS -> Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show()
                SignUpState.FAIL -> Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
                SignUpState.WEAK_PASSWORD -> Toast.makeText(context, "Password must be at least 6", Toast.LENGTH_SHORT).show()
            }
            signUpProgressBar.visibility = View.GONE
        })
    }
}
