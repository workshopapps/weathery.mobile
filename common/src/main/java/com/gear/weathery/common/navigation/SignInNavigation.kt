package com.gear.weathery.common.navigation

import androidx.navigation.NavController

interface SignInNavigation {
    fun navigateToSignIn(args:String = "Sign In Screen Here",navController: NavController)
}