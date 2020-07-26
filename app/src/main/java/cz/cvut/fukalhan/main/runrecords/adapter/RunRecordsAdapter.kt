package cz.cvut.fukalhan.main.runrecords.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.runrecords.fragment.RunRecordsFragment
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.runrecords.viewholder.RunRecordViewHolder
import cz.cvut.fukalhan.repository.entity.RunRecord
import org.koin.core.KoinComponent

class RunRecordsAdapter(private val userActivities: ArrayList<RunRecord>, private val resources: Resources, private val fragmentRun: RunRecordsFragment) : RecyclerView.Adapter<RunRecordViewHolder>(), KoinComponent {

    override fun getItemCount(): Int = userActivities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_run_record, parent, false)
        return RunRecordViewHolder(view)
    }

    override fun onBindViewHolder(holderRun: RunRecordViewHolder, position: Int) {
        val record = userActivities[position]
        holderRun.date.text = resources.getString(R.string.run_date, TimeFormatter.simpleDate.format(record.date))
        holderRun.distance.text = resources.getString(R.string.distance_km, record.distance.toString())
        holderRun.time.text = resources.getString(R.string.time, TimeFormatter.toHourMinSec(record.time))
        holderRun.tempo.text = resources.getString(R.string.tempo_min_km, TimeFormatter.toMinSec(record.pace))
        holderRun.itemView.setOnClickListener {
            if (holderRun.deleteButton.visibility == View.GONE) {
                holderRun.deleteButton.visibility = View.VISIBLE
            } else {
                holderRun.deleteButton.visibility = View.GONE
            }
        }
        holderRun.deleteButton.setOnClickListener {
            fragmentRun.makeDeleteRecordDialog(record.id, position)
        }
    }

    fun deleteRecordOnPosition(position: Int) {
        userActivities.removeAt(position)
        this.notifyItemRemoved(position)
    }
}