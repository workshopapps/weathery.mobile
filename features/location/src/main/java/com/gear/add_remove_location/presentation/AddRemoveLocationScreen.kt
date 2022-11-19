package com.gear.add_remove_location.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import com.gear.add_remove_location.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.presentation.components.LearnMore
import com.gear.add_remove_location.presentation.components.LocationSearchBar
import com.gear.add_remove_location.presentation.components.WeatherItem
import com.gear.add_remove_location.presentation.components.WeatherSearchItem
import com.gear.add_remove_location.presentation.ui.theme.*

@Composable
fun AddRemoveLocationScreen(onNavBack: () -> Unit, onAddItem: () -> Unit) {
    var state by remember { mutableStateOf(false) }
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
            color = Gray900
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
            color = Gray900
        )
        if (!state) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    WeatherItem(
                        imageResource = R.drawable.location_ic_scattered_thunderstorm,
                        location = "owerri",
                        time = "2.00PM",
                        weather = "Rainy",
                        color = Rainy
                    )
                }
                item {
                    WeatherItem(
                        imageResource = R.drawable.location_ic_cloudy,
                        location = "ibadan",
                        time = "12.00PM",
                        weather = "Cloudy",
                        color = Cloudy
                    )
                }
                item {
                    WeatherItem(
                        imageResource = R.drawable.location_ic_sunny,
                        location = "ghana",
                        time = "4.00PM",
                        weather = "Clear",
                        color = ClearNight
                    )
                }
                item {
                    WeatherItem(
                        imageResource = R.drawable.location_ic_clear_night,
                        location = "Kenya",
                        time = "10.00PM",
                        weather = "Clear",
                        color = Clear
                    )
                }
                item { LearnMore {} }
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
            ) { onAddItem() }
            Spacer(modifier = Modifier.height(16.dp))
            WeatherSearchItem(
                location = "Lagosanto FE",
                country = "Italy",
                time = "9.00pm",
                weather = "Clear",
                conditionBgRes = R.drawable.location_clear_night_bg,
                conditionFgRes = R.drawable.location_ic_halfmoon
            ) {}
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ScreenPrev() {
    AddRemoveLocationScreen({}, {})
}