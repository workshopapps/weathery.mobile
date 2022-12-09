package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.manage_location.components.LocationItem
import com.gear.add_remove_location.presentation.manage_location.components.drawDropShadow
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle
import com.gear.weathery.location.api.Location

@Composable
fun ViewAction(
    savedLocations: List<Location>,
) {
    Text(
        text = stringResource(R.string.saved_locations),
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
            Text(stringResource(R.string.click_on))
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(R.string.expand))
            Text(text = stringResource(R.string.to_add))
        }
    }

    LazyColumn {
        items(savedLocations) { location ->
            LocationItem(imageRes = R.drawable.location_ic_on, location = buildString {
                append(location.name)
                append(if (location.state.isNotBlank()) ", ${location.state}" else "")
            }, modifier = Modifier.padding(16.dp)
                .fillMaxWidth()
                .drawDropShadow(color = MaterialTheme.colors.primary)
                .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp))
                .padding(16.dp))
        }
    }
}