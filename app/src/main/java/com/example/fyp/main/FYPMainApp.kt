package com.example.fyp.main

import android.app.Application
import timber.log.Timber

class FYPMainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Starting Receiptly Application...")
    }
}
