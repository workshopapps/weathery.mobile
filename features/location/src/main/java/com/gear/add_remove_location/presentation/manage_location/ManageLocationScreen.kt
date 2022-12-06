package com.gear.add_remove_location.presentation.manage_location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.MenuActions
import com.gear.add_remove_location.presentation.manage_location.components.actions.EditAction
import com.gear.add_remove_location.presentation.manage_location.components.actions.SearchAction
import com.gear.add_remove_location.presentation.ui.theme.LocationTitleStyle


@Composable
fun ManageLocationScreen(
    viewModel: LocationViewModel,
    navController: NavController
) {
    var expandActions by remember { mutableStateOf(false) }
    val state = viewModel.manageScreenState.value
    val locations = state.locations
    val action = viewModel.screenState.value
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    IconButton(onClick = { expandActions = !expandActions }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            if (expandActions) {
                MenuActions(modifier = Modifier.align(Alignment.TopEnd)) {
                    viewModel.setAction(it)
                    expandActions = false

                    if (it == Action.SEARCH_SAVE) {
                        viewModel.setSearchState("")
                    }
                }
            }

            Column {
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
                            onSaveItemClicked = { index, location, isSelected ->
                                viewModel.saveItemSelected(index, location, isSelected)
                            },
                            saveLocations = {
                                viewModel.saveLocations()
                                navController.popBackStack()
                            },
                            cancelSave = { navController.popBackStack() }
                        )
                    }
                    Action.EDIT -> {
                        EditAction(
                            savedLocations = viewModel.savedLocations.value,
                            onDeleteItemSelected = { index, location, isSelected ->
                                viewModel.deleteItemSelected(
                                    index = index,
                                    location = location,
                                    isSelected = isSelected
                                )
                            },
                            cancelDelete = { navController.popBackStack() },
                            deleteLocations = {
                                viewModel.deleteLocations()
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class Action { SEARCH_SAVE, EDIT }