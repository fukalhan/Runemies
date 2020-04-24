package cz.cvut.fukalhan

import android.app.Application
import cz.cvut.fukalhan.repository.di.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(Module.loginModule)
            modules(Module.challengeModule)
            modules(Module.userModule)
        }
    }
}