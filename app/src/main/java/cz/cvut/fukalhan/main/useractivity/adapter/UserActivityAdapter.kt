package cz.cvut.fukalhan.main.useractivity.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.cvut.fukalhan.main.useractivity.fragment.ActivityCalendarFragment
import cz.cvut.fukalhan.main.useractivity.fragment.ActivityFragment
import cz.cvut.fukalhan.main.useractivity.fragment.ActivityOverviewFragment

class UserActivityAdapter(activityFragment: ActivityFragment): FragmentStateAdapter(activityFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ActivityOverviewFragment()
            1 -> ActivityCalendarFragment()
            else -> throw IllegalStateException()
        }
    }
}