package com.gear.add_remove_location.presentation

sealed class LocationScreen(val route: String){
    object Manage: LocationScreen(route = "manage_location_screen")
}
