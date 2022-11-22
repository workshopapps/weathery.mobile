package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface NotificationsNavigation {
    fun navigateToNotifications(args:String = "Dashboard Screen Here",navController: NavController)
}