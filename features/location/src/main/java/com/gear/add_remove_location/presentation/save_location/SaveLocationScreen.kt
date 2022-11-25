package com.gear.add_remove_location.presentation.save_location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.save_location.components.HourlyWeatherItem
import com.gear.add_remove_location.presentation.ui.theme.Gray900
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.presentation.ui.theme.Primary500

@Composable
fun SaveLocationScreen(navController: NavController, viewModel: LocationViewModel) {
    val state = viewModel.saveScreenState.value
    val country = state.country
    val location = state.name

    Column(Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = "navigate",
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(start = 24.dp, bottom = 24.dp, top = 8.dp)
        )
        Text(
            text = "Save Location",
            modifier = Modifier.padding(start = 24.dp),
            fontFamily = Outfit,
            fontSize = 24.sp,
            color = Gray900
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            HourlyWeatherItem(country, location)
            OutlinedButton(
                onClick = {
                    navController.popBackStack() },//change to add args to the manage screen
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding( 24.dp)
            ) {
                Text(text = "SAVE LOCATION", color = Primary500)
            }
        }
    }
}
