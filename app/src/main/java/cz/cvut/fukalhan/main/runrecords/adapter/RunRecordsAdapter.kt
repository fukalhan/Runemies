package cz.cvut.fukalhan.main.runrecords.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.runrecords.viewholder.IDeleteButtonListener
import cz.cvut.fukalhan.utils.TimeFormatter
import cz.cvut.fukalhan.main.runrecords.viewholder.RunRecordViewHolder
import cz.cvut.fukalhan.repository.entity.RunRecord
import cz.cvut.fukalhan.utils.ViewVisibility

class RunRecordsAdapter(private val records: ArrayList<RunRecord>, private val deleteButtonListener: IDeleteButtonListener) : RecyclerView.Adapter<RunRecordViewHolder>() {

    override fun getItemCount(): Int = records.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_run_record, parent, false)
        return RunRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunRecordViewHolder, position: Int) {
        val record = records[position]
        holder.date.text = holder.itemView.resources.getString(R.string.run_date, TimeFormatter.simpleDate.format(record.date))
        holder.distance.text = holder.itemView.resources.getString(R.string.distance_km, String.format("%.2f", record.distance))
        holder.time.text = holder.itemView.resources.getString(R.string.time, TimeFormatter.toHourMinSec(record.time))
        holder.tempo.text = holder.itemView.resources.getString(R.string.tempo_min_km, TimeFormatter.toMinSec(record.pace))
        holder.itemView.setOnClickListener {
            ViewVisibility.toggleVisibility(holder.deleteButton)
        }
        holder.deleteButton.setOnClickListener {
            deleteButtonListener.onClick(record.id, position)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun deleteRecordOnPosition(position: Int) {
        records.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, records.size)
        this.notifyDataSetChanged()
    }
}