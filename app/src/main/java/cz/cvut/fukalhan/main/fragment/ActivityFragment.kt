package cz.cvut.fukalhan.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.fragment.useractivity.UserActivityAdapter
import kotlinx.android.synthetic.main.fragment_activity.*

/**
 * A simple [Fragment] subclass.
 */
class ActivityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_activity, container, false)
        view
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        activityViewPager.adapter = UserActivityAdapter(this)
        TabLayoutMediator(activityTabLayout, activityViewPager) { tab, position ->
            tab.text = when(position){
                0 -> "OVERVIEW"
                1 -> "CALENDAR"
                else -> throw IllegalStateException()
            }
        }.attach()
    }

}
