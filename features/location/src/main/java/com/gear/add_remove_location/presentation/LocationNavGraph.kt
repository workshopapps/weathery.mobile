package com.gear.add_remove_location.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gear.add_remove_location.presentation.manage_location.ManageLocationScreen
import com.gear.add_remove_location.presentation.manage_location.SaveLocationScreen
import com.gear.add_remove_location.presentation.manage_location.util.EnterScreenAnimation

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    viewModel: LocationViewModel,
    onNavBack: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = LocationScreen.Save.route
    ) {

        composable(
            route = LocationScreen.Manage.route
        ) {
            EnterScreenAnimation {
                ManageLocationScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

        composable(
            route = LocationScreen.Save.route
        ) {
            EnterScreenAnimation {
                SaveLocationScreen(
                    onNavBack = onNavBack,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}