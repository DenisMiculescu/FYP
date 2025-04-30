package com.example.fyp.location

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class LocationTracker(
    private var locationClient: FusedLocationProviderClient
) : LocationService {

    @SuppressLint("MissingPermission")
    override fun getLocationFlow() = callbackFlow {
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                try {
                    trySend(result.lastLocation)
                } catch (e: Exception) {
                    Timber.tag("Error").e(e.message.toString())
                }
            }
        }

        locationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.getMainLooper())
            .addOnFailureListener { e ->
                close(e)
            }

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }
}