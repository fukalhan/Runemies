package cz.cvut.fukalhan.main.useractivity.adapter

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.common.TimeFormatter
import cz.cvut.fukalhan.main.useractivity.viewholder.UserActivityViewHolder
import cz.cvut.fukalhan.repository.entity.RunRecord
import org.koin.core.KoinComponent

class UserActivityAdapter(private val userActivities: List<RunRecord>, private val context: Context) : RecyclerView.Adapter<UserActivityViewHolder>(), KoinComponent {

    override fun getItemCount(): Int = userActivities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_run_record, parent, false)
        return UserActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserActivityViewHolder, position: Int) {
        val record = userActivities[position]
        holder.date.text = TimeFormatter.simpleDate.format(record.date)
        val res = context.resources
        holder.distance.text = res.getString(R.string.distance_km, record.distance.toString())
        holder.time.text = res.getString(R.string.time, TimeFormatter.toHourMinSec(record.time))
        holder.tempo.text = res.getString(R.string.tempo_min_km, TimeFormatter.toMinSec(record.tempo))
    }
}