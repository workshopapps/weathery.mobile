package com.gear.weathery.signin.utils

import androidx.navigation.NavController
import com.gear.weathery.common.navigation.SignInNavigation
import com.gear.weathery.signin.R

class SignInNavigationImpl:SignInNavigation {
    override fun navigateToSignIn(args: String, navController: NavController) {
        navController.navigate(R.id.signin_nav_graph)
    }
}
