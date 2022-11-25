package com.gear.add_remove_location.util

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.add_remove_location.R
import com.gear.weathery.common.navigation.AddRemoveLocationNavigation

class AddRemoveLocationNavigationImpl: AddRemoveLocationNavigation {
    override fun navigateToAddRemoveLocation(args: String, navController: NavController) {
        navController.navigate(R.id.location_nav_graph, bundleOf("args" to args))
    }
}