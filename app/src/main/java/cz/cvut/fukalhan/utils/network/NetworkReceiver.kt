package cz.cvut.fukalhan.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (NetworkUtil.isNetworkAvailable) {
            Log.e("network", "connected")
        } else {
            Log.e("network", "not connected")
        }
    }
}