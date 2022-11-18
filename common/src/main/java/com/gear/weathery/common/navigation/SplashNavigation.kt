package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface SplashNavigation {
    fun navigateToSplash(args:String = "Splash Screen Here", navController: NavController)

}