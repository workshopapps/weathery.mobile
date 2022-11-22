package com.gear.add_remove_location.presentation.save_location.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Outfit

@Composable
fun HourlyWeatherItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Lagos",
            fontFamily = Outfit,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.location_umbrella),
            contentDescription = "Rainy"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Rainy", fontFamily = Outfit, fontSize = 16.sp)
        Text(
            text = "Expect rain and scattered thunderstorms by 12:00pm",
            fontFamily = Outfit,
            fontSize = 40.sp
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "FLOODING RISK: HIGH",
            fontFamily = Outfit,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )//Replace with value from api
    }
}

@Preview
@Composable
fun HPrev() {
    HourlyWeatherItem()
}