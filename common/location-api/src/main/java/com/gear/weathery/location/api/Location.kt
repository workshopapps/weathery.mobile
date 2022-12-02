package com.gear.weathery.location.api

data class Location(
    val id: Int,
    val state: String,
    val name: String,
    val country: String,
    val longitude: Double,
    val latitude: Double
)