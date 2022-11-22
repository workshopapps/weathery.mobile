package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface AccountNavigation {
    fun navigateAccountProfile(args:String = "Account Profile Screen Here",navController: NavController)
}