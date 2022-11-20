package com.gear.weathery.dashboard.util

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.dashboard.R

class DashBoardNavigationImpl:DashBoardNavigation {
    override fun navigateToDashboard(args: String, navController: NavController) {
        navController.navigate(R.id.dashboard_nav_graph, bundleOf("args" to args))
    }
}