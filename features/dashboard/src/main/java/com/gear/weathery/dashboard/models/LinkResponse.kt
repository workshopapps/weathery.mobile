package com.gear.weathery.dashboard.models


import com.google.gson.annotations.SerializedName

data class LinkResponse(
    @SerializedName("link")
    val link: String?
)