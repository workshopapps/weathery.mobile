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
import com.gear.add_remove_location.presentation.LocationScreen
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.MenuActions
import com.gear.add_remove_location.presentation.manage_location.components.actions.ViewAction
import com.gear.add_remove_location.presentation.ui.theme.LocationTitleStyle

@Composable
fun SaveLocationScreen(
    onNavBack: () -> Unit,
    viewModel: LocationViewModel,
    navController: NavController
) {
    var expandActions by remember { mutableStateOf(false) }
    val state = viewModel.manageScreenState.value
    state.locations
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
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

                    if(it == Action.SEARCH_SAVE){
                        viewModel.setSearchState("")
                    }
                    navController.navigate(LocationScreen.Manage.route)
                }
            }

            Column {
                ViewAction(savedLocations = viewModel.savedLocations.value)
            }
        }
    }
}
