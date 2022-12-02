package com.gear.add_remove_location.data.responses.dto


import com.google.gson.annotations.SerializedName

data class LocationDtoItem(
    @SerializedName("admin1")
    val state: String,
    @SerializedName("country")
    val country: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String,
)