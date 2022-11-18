package com.gear.weathery.utils

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.R
import com.gear.weathery.common.navigation.SplashNavigation

class SplashNavigationImpl:SplashNavigation {
    override fun navigateToSplash(args: String, navController: NavController) {
        navController.navigate(R.id.app_navigation_xml, bundleOf("args" to args))
    }
}