package com.gear.add_remove_location.presentation.manage_location

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.gear.add_remove_location.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gear.add_remove_location.data.getColor
import com.gear.add_remove_location.data.getImageRes
import com.gear.add_remove_location.data.getWeather
import com.gear.add_remove_location.presentation.LocationScreen
import com.gear.add_remove_location.presentation.manage_location.components.LearnMore
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.WeatherItem
import com.gear.add_remove_location.presentation.manage_location.components.WeatherSearchItem
import com.gear.add_remove_location.presentation.ui.theme.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ManageLocationScreen(onNavBack: () -> Unit,navController: NavController) {
    var state by remember { mutableStateOf(false) }
    val weatherList = getWeather()
    Column(Modifier.fillMaxSize()) {
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
            if (it == "Lagos") {
                state = true
            }
        }
        Text(
            text = " Popular locations",
            modifier = Modifier.padding(start = 24.dp, bottom = 24.dp),
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        if (!state) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                itemsIndexed(weatherList, key = { _, item ->
                    item.hashCode()
                }) { _, weather ->
                    Row(
                        modifier = Modifier.animateItemPlacement(
                            tween(durationMillis = 250)
                        )
                    ) {
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    weatherList.remove(weather)
                                }
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.StartToEnd -> Color.White
                                    DismissDirection.EndToStart -> Color.Red
                                    null -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .fillMaxSize()
                                        .background(color, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteForever,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            },
                            dismissContent = {

                                WeatherItem(
                                    imageResource = getImageRes(weather.location),
                                    location = weather.location,
                                    time = weather.time,
                                    weather = weather.weather,
                                    color = getColor(weather.location)
                                )
                            },
                            directions = setOf(DismissDirection.EndToStart)
                        )
                    }
                }
            }
        }
        if (state) {

            WeatherSearchItem(
                location = "Lagos",
                country = "Nigeria",
                time = "2:00pm",
                weather = "Cloudy",
                conditionBgRes = R.drawable.location_cloudy_bg,
                conditionFgRes = R.drawable.location_ic_windy_cloud
            ) { navController.navigate(LocationScreen.Save.route) }
            Spacer(modifier = Modifier.height(16.dp))
            WeatherSearchItem(
                location = "Lagosanto FE",
                country = "Italy",
                time = "9.00pm",
                weather = "Clear",
                conditionBgRes = R.drawable.location_clear_night_bg,
                conditionFgRes = R.drawable.location_ic_halfmoon
            ) {navController.navigate(LocationScreen.Save.route)}
        }
        LearnMore {}
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPrev() {
    ManageLocationScreen({}, rememberNavController())
}