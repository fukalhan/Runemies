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
import cz.cvut.fukalhan.repository.login.states.SignInState
import kotlinx.android.synthetic.main.fragment_sign_in.*

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = SignInViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton.setOnClickListener {
            viewModel.signIn(emailSignIn.text.toString(), passwordSignIn.text.toString())
        }

        viewModel.signInState.observe(viewLifecycleOwner, Observer { state ->
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
}
