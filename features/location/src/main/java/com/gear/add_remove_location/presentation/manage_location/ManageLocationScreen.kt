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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gear.add_remove_location.R
import com.gear.add_remove_location.data.*
import com.gear.add_remove_location.presentation.LocationScreen
import com.gear.add_remove_location.presentation.manage_location.components.LearnMore
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.WeatherItem
import com.gear.add_remove_location.presentation.manage_location.components.WeatherSearchItem
import com.gear.add_remove_location.presentation.ui.theme.Outfit

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ManageLocationScreen(
    onNavBack: () -> Unit,
    navController: NavController,
) {
    var state by remember { mutableStateOf(false) }
    val weatherList = remember{ getWeather()}
    Column(Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = stringResource(R.string.navigate),
            modifier = Modifier
                .clickable { onNavBack() }
                .padding(start = 24.dp, bottom = 24.dp, top = 8.dp)
        )
        Text(
            text = stringResource(R.string.manageCities),
            modifier = Modifier.padding(start = 24.dp),
            fontFamily = Outfit,
            fontSize = 24.sp,
        )
        LocationSearchBar {
            if (it.contains("Lagos")) {
                state = true
            }
        }
        Text(
            text = stringResource(R.string.savedLocations),
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
                                    removeWeather(weather)
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
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .fillMaxSize()
                                        .background(color, RoundedCornerShape(8.dp)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(text = stringResource(R.string.delete), color = Color.White)
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteForever,
                                        contentDescription = stringResource(R.string.delete),
                                        tint = Color.White,
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
                location = stringResource(R.string.lagos),
                country = stringResource(R.string.nigeria),
                time = stringResource(R.string._2_pm),
                weather = stringResource(R.string.cloudy),
                conditionBgRes = R.drawable.location_cloudy_bg,
                conditionFgRes = R.drawable.location_ic_windy_cloud
            ) { navController.navigate(LocationScreen.Save.route) }
            Spacer(modifier = Modifier.height(16.dp))
            WeatherSearchItem(
                location = stringResource(R.string.lagosantofe),
                country = stringResource(R.string.italy),
                time = stringResource(R.string._9_pm),
                weather = stringResource(R.string.clear),
                conditionBgRes = R.drawable.location_clear_night_bg,
                conditionFgRes = R.drawable.location_ic_halfmoon
            ) {navController.navigate(LocationScreen.Save.route)}
        }
        LearnMore {}
    }
}