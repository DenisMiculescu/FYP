package com.example.fyp.data.models

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

data class Pharmacy(
    val name: String,
    val latLng: LatLng,
    val address: String
)

@Serializable
data class PlacesResponse(
    val results: List<PlaceResult>
)

@Serializable
data class PlaceResult(
    val name: String,
    val vicinity: String,
    val geometry: Geometry
)

@Serializable
data class Geometry(
    val location: Location
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)

