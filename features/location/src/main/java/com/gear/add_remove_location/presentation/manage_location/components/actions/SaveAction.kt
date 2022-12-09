package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.manage_location.SaveListItem
import com.gear.add_remove_location.presentation.manage_location.components.LocationItem
import com.gear.add_remove_location.presentation.manage_location.components.drawDropShadow
import com.gear.add_remove_location.presentation.ui.theme.ButtonTextStyle
import com.gear.add_remove_location.presentation.ui.theme.Gray500
import com.gear.add_remove_location.presentation.ui.theme.Primary500
import com.gear.weathery.location.api.Location

@Composable
fun SaveAction(
    locations: List<Location>,
    text: String,
    onItemSelected: (index: Int, location: Location, isSelected: Boolean) -> Unit,
    saveLocations: () -> Unit,
    cancelSave: () -> Unit
) {
    val query by remember { mutableStateOf(text) }
    Text(
        text = stringResource(R.string.locations_related) + "\"$query\"",
        color = Gray500,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    )

    var items by remember {
        mutableStateOf(
            locations.map { loc ->
                SaveListItem(
                    location = loc,
                    isSelected = false
                )
            }
        )
    }


    LazyColumn {
        items(items.size) { i ->

            LocationItem(imageRes = R.drawable.location_ic_on, location = buildString {
                append(items[i].location.name)
                append(if (items[i].location.state.isNotBlank()) ", ${items[i].location.state}" else "")
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .drawDropShadow(color = MaterialTheme.colors.primary)
                .background(
                    if (items[i].isSelected) Primary500.copy(0.75f) else MaterialTheme.colors.background,
                    RoundedCornerShape(8.dp)
                )
                .clickable {
                    items = items.mapIndexed { j, item ->
                        if (i == j) {
                            item.copy(isSelected = !item.isSelected)
                        } else item
                    }
                    onItemSelected(i, items[i].location, items[i].isSelected)
                }
                .padding(16.dp))
        }

        item {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                Row(Modifier.align(Alignment.Center)) {
                    OutlinedButton(onClick = { cancelSave() }, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = ButtonTextStyle,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(48.dp))
                    Button(
                        onClick = { saveLocations() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = ButtonTextStyle,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}