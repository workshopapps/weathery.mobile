package com.gear.weathery.setting

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.common.navigation.SettingsNavigation

class SettingsNavigationImpl: SettingsNavigation {
    override fun navigateToSettings(args: String, navController: NavController) {
        navController.navigate(R.id.settings_nav_graph, bundleOf("args" to args))
    }
}