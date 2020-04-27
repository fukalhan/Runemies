package cz.cvut.fukalhan.utils.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject

object NetworkUtil : KoinComponent {
    private val context: Context by inject()
    val connected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo != null && networkInfo.isConnected
            connected.postValue(isConnected)
            return isConnected
        }
}