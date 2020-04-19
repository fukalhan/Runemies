package cz.cvut.fukalhan.main.profile.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.ILoginNavigation
import cz.cvut.fukalhan.login.activity.LoginActivity
import cz.cvut.fukalhan.main.activity.MainActivity
import cz.cvut.fukalhan.shared.Settings
import cz.cvut.fukalhan.utils.network.NetworkUtil
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), ILoginNavigation {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NetworkUtil.connected.observe(viewLifecycleOwner, Observer {connected ->
            when(connected) {
                true -> Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val activity = activity as MainActivity
        if (activity.isUserInitialised()) {
            profileUsername.text = activity.user.displayName
        }
    }

    override fun logOut() {
        // TODO logout user
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}
