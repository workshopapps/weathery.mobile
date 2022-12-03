package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.presentation.manage_location.Action
import com.gear.add_remove_location.presentation.ui.theme.LocationItemStyle


@Composable
fun MenuActions(
    modifier: Modifier = Modifier,
    setMenuAction:(action: Action) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(end = 16.dp)
            .drawDropShadow(color = Color(0xFF4D5E6F))
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Column {
            MenuItem(imageVector = Icons.Outlined.AddLocation, action = "Add"){setMenuAction(Action.SEARCH_SAVE)}
            MenuItem(imageVector = Icons.Outlined.Edit, action = "Edit"){setMenuAction(Action.EDIT)}
        }
    }
}

@Composable
private fun MenuItem(
    imageVector: ImageVector,
    action: String,
    actionClick: () -> Unit
) {
    Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { actionClick() }){
        Icon(
            imageVector = imageVector,
            contentDescription = "Menu item",
            modifier = Modifier.padding(8.dp)
        )
        Text(text = action, style = LocationItemStyle, modifier = Modifier.padding(end = 8.dp))
    }
}