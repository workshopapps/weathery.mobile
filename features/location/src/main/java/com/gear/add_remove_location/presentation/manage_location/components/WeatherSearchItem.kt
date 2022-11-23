package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.presentation.ui.theme.*
import com.gear.add_remove_location.R

@Composable
fun WeatherSearchItem(
    location: String,
    country: String,
    time: String,
    weather: String,
    conditionBgRes: Int,
    conditionFgRes: Int,
    onAdd: () -> Unit
) {
    Card(
        border = BorderStroke(1.dp, Gray300),
        backgroundColor = Gray50,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .width(348.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.padding(bottom = 8.dp)) {
                Image(
                    painter = painterResource(id = conditionBgRes),
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentDescription = "Condition Background Image",
                    colorFilter = ColorFilter.tint(Rainy.copy(0.83f), blendMode = BlendMode.SrcAtop)
                )
                Image(
                    painter = painterResource(id = conditionFgRes),
                    contentDescription = "Condition ForegroundImage",
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp)
                )
            }


            Row {
                Text(
                    text = "$location,",
                    fontFamily = Outfit,
                    fontSize = 18.sp,
                    color = Gray900
                )
                Text(
                    text = country,
                    fontFamily = Outfit,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = time,
                fontFamily = Outfit,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weather,
                    fontFamily = Outfit,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.clickable { onAdd() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add button",
                        modifier = Modifier.size(24.dp),
                        tint = Primary500
                    )
                    Text(
                        text = "Add",
                        fontFamily = Outfit,
                        color = Primary500,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun LocationSearchItemPrev() {
    WeatherSearchItem(
        location = "Nairobi",
        country = "Kenya",
        time = "8.00pm",
        weather = "Cloudy",
        conditionBgRes = R.drawable.location_cloudy_bg,
        conditionFgRes = R.drawable.location_ic_windy_cloud
    ) {}
}