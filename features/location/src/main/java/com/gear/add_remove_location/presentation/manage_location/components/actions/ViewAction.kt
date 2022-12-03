package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.manage_location.components.LocationItem
import com.gear.add_remove_location.presentation.ui.theme.Gray500
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle
import com.gear.weathery.location.api.Location

@Composable
fun ViewAction(
    savedLocations: List<Location>,
) {
    Text(
        text = "Saved locations",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        style = LocationSubStyle
    )

    if (savedLocations.isEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Click on ", color = Gray500)
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu", tint = Gray500)
            Text(text = "to add locations", color = Gray500)
        }
    }

    LazyColumn {
        items(savedLocations) { location ->
            LocationItem(imageRes = R.drawable.location_ic_on, location = buildString {
                append(location.name)
                append(if (location.state.isNotBlank()) ", ${location.state}" else "")
            }) {}
        }
    }
}