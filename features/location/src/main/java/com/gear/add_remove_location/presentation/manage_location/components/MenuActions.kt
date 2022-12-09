package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.LocationItemStyle

@Composable
fun CustomMenuItem(
    imageVector: ImageVector,
    action: String,
    actionClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { actionClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.menu_item),
            modifier = Modifier.padding(end = 8.dp)

        )
        Text(text = action, style = LocationItemStyle)
    }
}