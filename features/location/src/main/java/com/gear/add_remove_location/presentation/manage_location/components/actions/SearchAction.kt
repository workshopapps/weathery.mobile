package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.SearchResultsWidget
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle
import com.gear.weathery.location.api.Location

@Composable
fun SearchAction(
    locations: List<Location>,
    isOnSearch: Boolean,
    text: String,
    onLocationSearch: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onSaveItemClicked: (index: Int, location: Location, isSelected: Boolean) -> Unit,
    saveLocations: () -> Unit,
    cancelSave: () -> Unit
) {
    Text(
        text = "Add a new location",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        style = LocationSubStyle
    )


    if (isOnSearch) {LocationSearchBar(
        modifier = Modifier.padding(top = 16.dp), text = text,
    ) { onLocationSearch(it) }
        Box(
            Modifier
                .fillMaxSize()
        ) {
            if (text.isNotBlank()) {
                SearchResultsWidget(
                    modifier = Modifier.align(Alignment.TopCenter),
                    list = locations.map {
                        buildString {
                            append(it.name)
                            append(if (it.country.isNotBlank()) ", ${it.country}" else "")
                        }
                    }.distinct()
                ) {
                    onLocationSelected(it)
                }
            }
        }
    } else SaveAction(
        locations = locations,
        text = text,
        { index, location, isSelected ->
            onSaveItemClicked(index, location, isSelected)
        },
        { saveLocations() },
        { cancelSave() }
    )
}