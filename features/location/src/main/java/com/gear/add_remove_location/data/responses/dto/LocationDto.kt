package com.gear.add_remove_location.data.responses.dto


import com.google.gson.annotations.SerializedName


data class LocationDto(
    @SerializedName("results")
    val results: List<LocationDtoItem>
)