package cz.cvut.fukalhan.main.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.cvut.fukalhan.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCalendar()
    }

    private fun setCalendar() {
        calendar.date = Calendar.getInstance().timeInMillis
        /*run_date.text = Date.simpleDate.format(calendar.date).toString()
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            run_date.text = Date.simpleDate.format(calendar.date).toString()
        }*/
    }
}
