package com.gear.add_remove_location.presentation.manage_location

import com.gear.weathery.location.api.Location

data class ManageScreenState(
    val locations: List<Location> = emptyList(),
    val isSuccessful: Boolean = false,
    val error: String = ""
)