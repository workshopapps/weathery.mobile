package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface BoardingNavigation {

    fun navigateToBoarding(args:String = "Boarding Screen Here",navController: NavController)

}