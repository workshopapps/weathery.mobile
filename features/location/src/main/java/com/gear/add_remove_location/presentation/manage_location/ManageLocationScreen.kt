package com.gear.add_remove_location.presentation.manage_location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.CustomMenuItem
import com.gear.add_remove_location.presentation.manage_location.components.actions.DeleteAction
import com.gear.add_remove_location.presentation.manage_location.components.actions.SearchAction
import com.gear.add_remove_location.presentation.manage_location.components.drawDropShadow
import com.gear.add_remove_location.presentation.ui.theme.LocationTitleStyle
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ManageLocationScreen(
    viewModel: LocationViewModel,
    navController: NavController
) {
    //Boolean to check if menu is expanded
    var expanded by remember { mutableStateOf(false) }
    //Gets the current location values and loading state
    val state = viewModel.manageScreenState.value
    val locations = state.locations
    //Gets the value of the current screen after pressing on a menu item
    val action = viewModel.screenState.value

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    //Displays the snackbar
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LocationViewModel.UIEvent.ShowSearchErrors -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.message.asString(context)
                    )
                }
            }
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigation)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.location),
                        style = LocationTitleStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(
                                R.string.menu
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            Box(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .drawDropShadow(color = MaterialTheme.colors.primary)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp))
                    .align(Alignment.TopEnd)
            ) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    CustomMenuItem(
                        imageVector = Icons.Outlined.AddLocation,
                        action = stringResource(R.string.add)
                    ) {
                        viewModel.setAction(Action.SEARCH_SAVE)
                        viewModel.setSearchState("")
                    }
                    CustomMenuItem(
                        imageVector = Icons.Outlined.DeleteForever,
                        action = stringResource(R.string.delete)
                    ) {
                        viewModel.setAction(Action.EDIT)
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
                        DeleteAction(
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