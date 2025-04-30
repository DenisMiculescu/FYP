package com.example.fyp.ui.screens.map

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    permissions: Boolean,
) {
    val context = LocalContext.current
    val apiKey = context.getString(R.string.http_key)
    val pharmacies by mapViewModel.pharmacies.collectAsState()
    val currentLocation by mapViewModel.currentLatLng.collectAsState()

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
    var hasAnimatedToUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (permissions) {
            mapViewModel.initializePlaces(context, apiKey)
            mapViewModel.getLocationUpdates()
        }
    }

    LaunchedEffect(currentLocation) {
        if (permissions && currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            if (!hasAnimatedToUser) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(currentLocation, 14f)
                )
                hasAnimatedToUser = true
                mapViewModel.loadNearbyPharmacies(apiKey)
            }
        }
    }

    var pharmacyIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(Unit) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.pharmacy_marker)
        drawable?.let {
            val bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            pharmacyIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
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
                snippet = "Your current location"
            )

            if (pharmacyIcon != null) {
                pharmacies.forEach { place ->
                    Marker(
                        state = MarkerState(position = place.latLng),
                        title = place.name,
                        snippet = place.address,
                        icon = pharmacyIcon
                    )
                }
            }
        }
    }
}
