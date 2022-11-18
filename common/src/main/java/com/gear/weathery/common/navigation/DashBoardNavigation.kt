package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface DashBoardNavigation {
    fun navigateToDashboard(args:String = "Dashboard Screen Here",navController: NavController)
}