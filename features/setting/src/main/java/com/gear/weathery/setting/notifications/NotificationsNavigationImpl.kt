package com.gear.weathery.setting.notifications

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.common.navigation.NotificationsNavigation
import com.gear.weathery.setting.R

class NotificationsNavigationImpl:NotificationsNavigation {
    override fun navigateToNotifications(args: String, navController: NavController) {
        navController.navigate(R.id.notifications_nav_graph, bundleOf("args" to args))
    }
}