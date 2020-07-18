package cz.cvut.fukalhan.main.profile.fragment

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ActivityGraphFormatter : ValueFormatter() {
    private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = value.toInt().toString()
        val year = date.substring(date.length - 4)
        val month = date.substring(0, date.length - 4).toInt()
        return "${months[month - 1]} $year"
    }
}