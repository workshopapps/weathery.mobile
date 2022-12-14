package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.SearchIndicator
import com.gear.add_remove_location.presentation.manage_location.components.SearchResultsWidget
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle
import com.gear.weathery.location.api.Location

@Composable
fun SearchAction(
    locations: List<Location>,
    isOnSearch: Boolean,
    text: String,
    isLoading: Boolean,
    onLocationSearch: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onSaveItemClicked: (index: Int, location: Location, isSelected: Boolean) -> Unit,
    saveLocations: () -> Unit,
    cancelSave: () -> Unit
) {

    Text(
        text = stringResource(R.string.add_new),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        style = LocationSubStyle
    )


    Crossfade(targetState = isOnSearch) { onSearch ->
        Column {
            when (onSearch) {
                true -> {
                    LocationSearchBar(
                        modifier = Modifier.padding(top = 16.dp), text = text,
                        onSearch = { onLocationSearch(it) }, indicator = {
                            if (text.length >= 3) {
                                if (isLoading) {
                                    SearchIndicator()
                                }
                            }
                        })
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
                }
                false -> {
                    SaveAction(
                        locations = locations,
                        text = text,
                        { index, location, isSelected ->
                            onSaveItemClicked(index, location, isSelected)
                        },
                        { saveLocations() },
                        { cancelSave() }
                    )
                }
            }
        }
    }
}