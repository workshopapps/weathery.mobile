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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gear.add_remove_location.presentation.LocationScreen
import com.gear.add_remove_location.presentation.LocationViewModel
import com.gear.add_remove_location.presentation.manage_location.components.CustomMenuItem
import com.gear.add_remove_location.presentation.manage_location.components.actions.ViewAction
import com.gear.add_remove_location.presentation.manage_location.components.drawDropShadow
import com.gear.add_remove_location.presentation.ui.theme.LocationTitleStyle

@Composable
fun SaveLocationScreen(
    onNavBack: () -> Unit,
    viewModel: LocationViewModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
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
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .drawDropShadow(color = MaterialTheme.colors.primary)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp))
                    .align(Alignment.TopEnd)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false},
                modifier = Modifier) {
                    CustomMenuItem(
                    imageVector = Icons.Outlined.AddLocation,
                    action = "Add"
                ) {
                        viewModel.setAction(Action.SEARCH_SAVE)
                        navController.navigate(LocationScreen.Manage.route)
                    }
                    CustomMenuItem(
                        imageVector = Icons.Outlined.DeleteForever,
                        action = "Delete"
                    ) {
                        viewModel.setAction(Action.EDIT)
                        navController.navigate(LocationScreen.Manage.route)
                    }
                }
            }


            Column {
                ViewAction(savedLocations = viewModel.savedLocations.value)
            }
        }
    }
}
