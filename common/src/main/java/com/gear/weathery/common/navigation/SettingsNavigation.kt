package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface SettingsNavigation {
    fun navigateToSettings(args:String = "Dashboard Screen Here",navController: NavController)
}