package com.sh.michael.simple_notepad.app

import android.app.Application
import com.sh.michael.simple_notepad.BuildConfig
import com.sh.michael.simple_notepad.app.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AndroidApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@AndroidApplication)
            modules(appModules)
        }
    }
}