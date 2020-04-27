package cz.cvut.fukalhan.login.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.cvut.fukalhan.login.fragment.SignInFragment
import cz.cvut.fukalhan.login.fragment.SignUpFragment
import java.lang.IllegalStateException

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
