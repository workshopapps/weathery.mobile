package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface AddRemoveLocationNavigation {
    fun navigateToAddRemoveLocation(
        args: String = "Boarding Screen Here",
        navController: NavController
    )
}