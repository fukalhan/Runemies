package cz.cvut.fukalhan.login.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.cvut.fukalhan.login.signin.SignInFragment
import cz.cvut.fukalhan.login.signup.SignUpFragment
import java.lang.IllegalStateException

/** Handle navigation between sign in and sign up screens */
class LoginAdapter(loginActivity: FragmentActivity) : FragmentStateAdapter(loginActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignInFragment()
            1 -> SignUpFragment()
            else -> throw IllegalStateException()
        }
    }
}
