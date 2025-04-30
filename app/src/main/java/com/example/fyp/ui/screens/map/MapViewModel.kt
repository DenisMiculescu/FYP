package com.example.fyp.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.models.Pharmacy
import com.example.fyp.data.models.PlacesResponse
import com.example.fyp.location.LocationService
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationTracker: LocationService
) : ViewModel() {

    private val _currentLatLng = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLatLng: StateFlow<LatLng> get() = _currentLatLng

    private lateinit var placesClient: PlacesClient

    private val _pharmacies = MutableStateFlow<List<Pharmacy>>(emptyList())
    val pharmacies: StateFlow<List<Pharmacy>> get() = _pharmacies

    fun initializePlaces(context: Context, apiKey: String) {
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, apiKey)
        }
        placesClient = Places.createClient(context)
        Timber.i("Places SDK initialized")
    }

    private fun setCurrentLatLng(latLng: LatLng) {
        Timber.i("Updating current location to: $latLng")
        _currentLatLng.value = latLng
    }

    fun getLocationUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            locationTracker.getLocationFlow().collect {
                Timber.i("Received location update: $it")
                it?.let { location ->
                    setCurrentLatLng(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    fun loadNearbyPharmacies(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val loc = currentLatLng.value
            val client = OkHttpClient()
            val url =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=${loc.latitude},${loc.longitude}" +
                        "&radius=50000&type=pharmacy&key=$apiKey"
            Timber.i("API_KEYY = $apiKey")
            val request = Request.Builder().url(url).build()
            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                Timber.i("Nearby Search Raw Response: $body")
                val json = Json { ignoreUnknownKeys = true }
                val parsed = json.decodeFromString<PlacesResponse>(body ?: "")
                _pharmacies.value = parsed.results.map {
                    Pharmacy(
                        name = it.name,
                        latLng = LatLng(it.geometry.location.lat, it.geometry.location.lng),
                        address = it.vicinity
                    )
                }
            } catch (e: Exception) {
                Timber.e("Failed to fetch nearby pharmacies: ${e.message}")
            }
        }
    }

}
