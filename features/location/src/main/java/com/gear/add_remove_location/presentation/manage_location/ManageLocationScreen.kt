package com.gear.add_remove_location.presentation.manage_location

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.actions.SearchAction
import com.gear.add_remove_location.presentation.ui.theme.LocationTitleStyle


@Composable
fun ManageLocationScreen(
    onNavBack: () -> Unit,
    viewModel: LocationViewModel,
) {

    val state = viewModel.manageScreenState.value
    val locations = state.locations
    val action by remember { mutableStateOf(Action.SEARCH_SAVE) }
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { onNavBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigation"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Location",
                        style = LocationTitleStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (action) {
                Action.SEARCH_SAVE -> {
                    SearchAction(
                        locations = locations,
                        isOnSearch = viewModel.isOnSearchState.value,
                        onLocationSearch = {
                            viewModel.onLocationSearch(it)
                        },
                        text = viewModel.searchTextState.value,
                        onLocationSelected = {
                            viewModel.setSearchState(it)
                        },
                        onSaveItemClicked = {index, location, isSelected ->
                            viewModel.saveItemSelected(index,location,isSelected)
                        },
                        saveLocations = {viewModel.saveLocations()}
                    )
                }
                Action.EDIT -> {}
                Action.VIEW -> {}
            }
        }
    }
}

enum class Action { SEARCH_SAVE, EDIT, VIEW }