package com.example.fyp.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import timber.log.Timber

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    permissions: Boolean,
) {
    val context = LocalContext.current
    val pharmacies by mapViewModel.pharmacies.collectAsState()
    val currentLocation by mapViewModel.currentLatLng.collectAsState()
    val apiKey = context.getString(R.string.places_api_key)

    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = permissions,
            compassEnabled = true,
            mapToolbarEnabled = true
        )
    }

    val mapStyleOptions = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }

    val properties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = permissions,
            mapStyleOptions = mapStyleOptions
        )
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(true) {
        if (permissions) {
            mapViewModel.initializePlaces(context, apiKey)
            mapViewModel.getLocationUpdates()
        }
    }

    LaunchedEffect(currentLocation) {
        if (permissions && currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            Timber.i("Location available: $currentLocation")
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
            mapViewModel.loadNearbyPharmacies(apiKey)
        }
    }

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            Marker(
                state = MarkerState(position = currentLocation),
                title = "You",
                snippet = "Your current location",
            )
            pharmacies.forEach { place ->
                Marker(
                    state = MarkerState(position = place.latLng),
                    title = place.name,
                    snippet = place.address
                )
            }
        }
    }
}
