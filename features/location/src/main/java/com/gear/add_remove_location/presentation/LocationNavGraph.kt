package com.gear.add_remove_location.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gear.add_remove_location.presentation.manage_location.ManageLocationScreen

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    viewModel: LocationViewModel,
    onNavBack: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = LocationScreen.Manage.route
    ) {
        composable(
            route = LocationScreen.Manage.route
        ) {
            ManageLocationScreen(
                onNavBack = onNavBack,
                viewModel = viewModel
            )
        }
    }
}