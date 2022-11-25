package com.gear.add_remove_location.presentation.manage_location

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gear.add_remove_location.presentation.LocationScreen
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.LearnMore
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.WeatherSearchItem
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import java.util.*


@Composable
fun ManageLocationScreen(
    onNavBack: () -> Unit,
    navController: NavController,
    viewModel: LocationViewModel,
) {

    val state = viewModel.manageScreenState.value
    val locations = state.locations

    Column(Modifier.fillMaxSize()) {
        val context = LocalContext.current
        Icon(
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = "navigate",
            modifier = Modifier
                .clickable { onNavBack() }
                .padding(start = 24.dp, bottom = 24.dp, top = 8.dp)
        )
        Text(
            text = "Manage cities",
            modifier = Modifier.padding(start = 24.dp),
            fontFamily = Outfit,
            fontSize = 24.sp,
        )
        LocationSearchBar {
            viewModel.onLocationSearch(it)
        }
        Text(
            text = "Saved locations",
            modifier = Modifier.padding(start = 24.dp, bottom = 24.dp),
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Box(Modifier.fillMaxSize()) {
            if (locations.any()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(locations) { location ->
                        val loc = Locale("", location.country)
                        val country = loc.displayCountry
                        WeatherSearchItem(location = location.name, country = country) {
                            viewModel.setLocationData(location.name,country)
                            navController.navigate(LocationScreen.Save.route)
                        }
                    }
                }
            }

            LearnMore(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)) { Toast.makeText(context, "check", Toast.LENGTH_LONG).show() }
        }
    }
}