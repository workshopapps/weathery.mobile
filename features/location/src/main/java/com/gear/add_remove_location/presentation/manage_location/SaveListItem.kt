package com.gear.add_remove_location.presentation.manage_location

import com.gear.weathery.location.api.Location

data class SaveListItem(
    val location: Location,
    val isSelected: Boolean
)
