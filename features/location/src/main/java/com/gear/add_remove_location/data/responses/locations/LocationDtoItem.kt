package com.gear.add_remove_location.data.responses.locations

import com.google.gson.annotations.SerializedName

data class LocationDtoItem(
    @SerializedName("country")
    val country: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("state")
    val state: String?
)