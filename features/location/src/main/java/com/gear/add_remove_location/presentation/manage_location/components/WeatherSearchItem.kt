package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.presentation.ui.theme.Gray300
import com.gear.add_remove_location.presentation.ui.theme.Gray50
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.presentation.ui.theme.Primary500
import kotlin.random.Random

@Composable
fun WeatherSearchItem(
    location: String,
    country: String,
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

            Row(Modifier.padding(top = 16.dp)) {
                Text(
                    text = "$location,",
                    fontFamily = Outfit,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = country,
                    fontFamily = Outfit,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val timeOfDay = Random.nextInt(1,12)
                Text(
                    text = "${timeOfDay}.00pm",
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
    ) {}
}