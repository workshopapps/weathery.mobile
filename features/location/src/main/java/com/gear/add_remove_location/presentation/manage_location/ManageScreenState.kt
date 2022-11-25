package com.gear.add_remove_location.presentation.manage_location

import com.gear.add_remove_location.data.responses.locations.LocationDtoItem

data class ManageScreenState(
    val locations: List<LocationDtoItem> = emptyList(),
    val isSuccessful: Boolean = false,
    val error: String = ""
)