package com.gear.add_remove_location.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gear.add_remove_location.presentation.manage_location.ManageLocationScreen
import com.gear.add_remove_location.presentation.save_location.SaveLocationScreen

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    onNavBack: () -> Unit
) {

    //Change to hiltVm in implementation

            NavHost(
        navController = navController,
        startDestination = LocationScreen.Manage.route
    ) {
        composable(
            route = LocationScreen.Manage.route
        ) {
            ManageLocationScreen(onNavBack = onNavBack, navController = navController)
        }
        composable(
            route = LocationScreen.Save.route
        ) {
            SaveLocationScreen(navController = navController)
        }
    }
}