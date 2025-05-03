package com.example.fyp.ui.screens.map

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    var radius by remember { mutableFloatStateOf(5000f) }
    var sliderRadius by remember { mutableFloatStateOf(5000f) }
    var pharmacyIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    var hasAnimatedToUser by remember { mutableStateOf(false) }


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

    LaunchedEffect(Unit) {
        if (permissions) {
            mapViewModel.initializePlaces(context, apiKey)
            mapViewModel.getLocationUpdates()
        }
        val drawable = ContextCompat.getDrawable(context, R.drawable.pharmacy_marker)
        drawable?.let {
            val bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            pharmacyIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    LaunchedEffect(radius, currentLocation) {
        if (permissions && currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            if (!hasAnimatedToUser) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(currentLocation, 14f)
                )
                hasAnimatedToUser = true
            }
            mapViewModel.loadNearbyPharmacies(apiKey, radius.toInt())
        }
    }


    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Search Radius: ${radius.toInt()}m")
            Slider(
                value = sliderRadius,
                onValueChange = { sliderRadius = it },
                onValueChangeFinished = {
                    radius = sliderRadius
                    mapViewModel.loadNearbyPharmacies(apiKey, radius.toInt())
                },
                valueRange = 1000f..20000f,
                steps = 3
            )
        }
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            Marker(
                state = MarkerState(position = currentLocation),
                title = "You",
                snippet = "Your current location"
            )
            Circle(
                center = currentLocation,
                radius = radius.toDouble(),
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                strokeColor = MaterialTheme.colorScheme.primary,
                strokeWidth = 2f
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
