package com.gear.weathery.utils

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.gear.weathery.R
import com.gear.weathery.common.navigation.AccountNavigation
import com.gear.weathery.common.navigation.DashBoardNavigation


class AccountProfileNavigationImpl:AccountNavigation {
    override fun navigateAccountProfile(args: String, navController: NavController) {
        navController.navigate(R.id.account_nav_graph, bundleOf("args" to args))
    }

}