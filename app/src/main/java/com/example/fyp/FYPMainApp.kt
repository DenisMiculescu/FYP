package com.example.fyp

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FYPMainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Starting Receiptly Application...")
        Firebase.initialize(context = this)
        Places.initialize(applicationContext, getString(R.string.places_api_key))
    }
}
