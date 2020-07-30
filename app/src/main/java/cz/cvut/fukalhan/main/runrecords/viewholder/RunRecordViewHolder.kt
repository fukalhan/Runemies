package cz.cvut.fukalhan.main.runrecords.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_run_record.view.*

class RunRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var date: TextView = itemView.run_record_date
    var distance: TextView = itemView.run_record_distance
    var time: TextView = itemView.run_record_time
    var tempo: TextView = itemView.run_record_tempo
    var deleteButton: Button = itemView.delete_record
}