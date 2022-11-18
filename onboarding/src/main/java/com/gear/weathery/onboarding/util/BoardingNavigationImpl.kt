package com.gear.weathery.onboarding.util

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.onboarding.R

class BoardingNavigationImpl:BoardingNavigation {
    override fun navigateToBoarding(args: String, navController: NavController) {
        navController.navigate(R.id.boarding_nav_graph, bundleOf("args" to args))
    }
}