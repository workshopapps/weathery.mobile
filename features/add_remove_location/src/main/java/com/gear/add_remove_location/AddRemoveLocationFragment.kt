package com.gear.add_remove_location

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.add_remove_location.presentation.AddRemoveLocationScreen
import com.gear.add_remove_location.presentation.ui.theme.LocationWeatheryTheme
import com.gear.weathery.common.navigation.DashBoardNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddRemoveLocationFragment: Fragment(R.layout.fragment_add_remove_location) {

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.location_compose_view).setContent {
            LocationWeatheryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AddRemoveLocationScreen(onNavBack = { dashBoardNavigation.navigateToDashboard(navController = findNavController()) }) {
                        //To navigate to the the dashboard: with some args to be added
                        dashBoardNavigation.navigateToDashboard(navController = findNavController())
                    }
                }
            }
        }
    }
}