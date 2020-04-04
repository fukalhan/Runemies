package cz.cvut.fukalhan.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.login.viewmodel.SignUpViewModel
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
        signUpButton.setOnClickListener {
            signUpProgressBar.visibility = View.VISIBLE
            viewModel.signUp("${emailSignUp.text}", passwordSignUp.text.toString(), usernameSignUp.text.toString())
        }

        viewModel.signUpState.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                true -> Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
            signUpProgressBar.visibility = View.GONE
        })
    }
}
