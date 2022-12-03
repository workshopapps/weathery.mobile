package com.gear.add_remove_location.data.responses.dto


import com.gear.weathery.location.api.Location
import com.google.gson.annotations.SerializedName

data class LocationDtoItem(
    @SerializedName("admin1")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String,
) {
    fun toLocations(): Location {
        return Location(
            id = 0,
            name = name,
            state = state ?: "",
            country = country ?: "",
            latitude = latitude,
            longitude = longitude
        )
    }
}