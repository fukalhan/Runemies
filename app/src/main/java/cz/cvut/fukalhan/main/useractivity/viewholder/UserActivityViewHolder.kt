package cz.cvut.fukalhan.main.useractivity.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_run_record.view.*

class UserActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var date: TextView = itemView.run_record_date
    var distance: TextView = itemView.run_record_distance
    var time: TextView = itemView.run_record_time
    var tempo: TextView = itemView.run_record_tempo
}