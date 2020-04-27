package cz.cvut.fukalhan.shared

import android.content.Context
import org.koin.core.KoinComponent
import org.koin.core.inject

object Settings : KoinComponent {
    private const val USERNAME = "username"
    private val context: Context by inject()
    private val sharedPreferences = context.getSharedPreferences("runemies_preferences", Context.MODE_PRIVATE)

    var username: String?
        get() {
            return sharedPreferences.getString(USERNAME, "")
        }
        set(value) {
            sharedPreferences.edit().putString(USERNAME, value).apply()
        }
}