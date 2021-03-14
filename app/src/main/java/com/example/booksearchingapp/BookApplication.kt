package com.example.booksearchingapp

import android.app.Application
import com.example.booksearchingapp.di.koinModuleApp
import com.example.booksearchingapp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class BookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    override fun onTerminate() {
        terminateKoin()
        super.onTerminate()
    }

    private fun terminateKoin() {
        stopKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BookApplication)
            modules(networkModule + koinModuleApp)
        }
    }
}