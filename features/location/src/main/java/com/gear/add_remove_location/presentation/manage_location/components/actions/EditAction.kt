package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle
import com.gear.add_remove_location.presentation.ui.theme.Primary500
import com.gear.weathery.location.api.Location

@Composable
fun EditAction(
    savedLocations: List<Location>,
    onDeleteItemSelected: (index: Int, location: Location, isSelected: Boolean) -> Unit,
    cancelDelete: () -> Unit,
    deleteLocations: () -> Unit
) {
    Text(
        text = stringResource(R.string.delete_locations),
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

    var items by remember{
        mutableStateOf(
            savedLocations.map { loc ->
                SaveListItem(
                    location = loc,
                    isSelected = false
                )
            }
        )
    }


    LazyColumn {
        items(items.size) { i ->

            Box {
                LocationItem(imageRes = R.drawable.location_ic_on, location = buildString {
                    append(items[i].location.name)
                    append(if (items[i].location.state.isNotBlank()) ", ${items[i].location.state}" else "")
                }, modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .drawDropShadow(color = MaterialTheme.colors.primary)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .padding(end = 8.dp))

                Checkbox(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(16.dp)
                        .align(Alignment.CenterEnd),
                    checked = items[i].isSelected,
                    onCheckedChange = {
                        items = items.mapIndexed { j, item ->
                            if (i == j) {
                                item.copy(isSelected = it)
                            } else item
                        }
                        onDeleteItemSelected(i, items[i].location, items[i].isSelected)
                    }
                )
            }
        }

        item {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                Row(Modifier.align(Alignment.Center)) {
                    OutlinedButton(onClick = { cancelDelete() }, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = ButtonTextStyle,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(48.dp))
                    Button(
                        onClick = { deleteLocations() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Primary500)
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = ButtonTextStyle,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}