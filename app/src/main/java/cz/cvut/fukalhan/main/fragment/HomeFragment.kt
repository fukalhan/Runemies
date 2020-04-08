package cz.cvut.fukalhan.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), ILoginNavigation {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOutButton.setOnClickListener {
            logOut()
        }
    }

    override fun logOut() {
        // TODO logout user
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}
