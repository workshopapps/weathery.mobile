package com.gear.add_remove_location.presentation.manage_location.components.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.data.responses.dto.LocationDtoItem
import com.gear.add_remove_location.presentation.manage_location.components.LocationSearchBar
import com.gear.add_remove_location.presentation.manage_location.components.SearchResultsWidget
import com.gear.add_remove_location.presentation.ui.theme.LocationSubStyle

@Composable
fun SearchAction(
    locations: List<LocationDtoItem>,
    onLocationSearch: (String) -> Unit
) {
    Text(
        text = "Add a new location",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        style = LocationSubStyle
    )

    LocationSearchBar(modifier = Modifier.padding(top = 16.dp))
    {
        onLocationSearch(it)
    }
    Box(
        Modifier
            .background(Color(0xFFD9D9D9).copy(0.15f))
            .fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .height(32.dp)
                .background(Color.White)
                .fillMaxWidth()
                .align(
                    Alignment.TopStart
                )
        )
        SearchResultsWidget(
            modifier = Modifier.align(Alignment.TopCenter),
            list = locations.map {
                buildString {
                    append(it.name)
                    append(if (it.country?.isNotBlank() == true) ", ${it.country}" else "")
                }
            }.distinct()
        ) {
            onLocationSearch(it)
        }
    }
}