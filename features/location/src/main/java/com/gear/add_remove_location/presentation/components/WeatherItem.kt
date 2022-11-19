package com.gear.add_remove_location.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.presentation.ui.theme.Rainy
import java.util.*
import com.gear.add_remove_location.R

@Composable
fun WeatherItem(
    modifier: Modifier = Modifier,
    imageResource: Int,
    location: String,
    time: String,
    weather: String,
    color: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(color, RoundedCornerShape(8.dp))
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${location.uppercase(Locale.getDefault())} . $time",
                fontFamily = Outfit,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = weather,
                fontFamily = Outfit,
                fontSize = 24.sp,
                color = Color.White
            )
        }
        Image(painter = painterResource(id = imageResource), contentDescription = null)
    }
}

@Preview
@Composable
fun AddRemovePrev() {
    WeatherItem(
        imageResource = R.drawable.location_ic_scattered_thunderstorm,
        location = "lagos",
        time = "9.00p.m",
        weather = "Rainy",
        color = Rainy
    )
}