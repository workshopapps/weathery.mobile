package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchResultsWidget(
    list: List<String>,
    modifier: Modifier = Modifier,
    locationClick: (location: String) -> Unit
) {
    BoxWithConstraints(
        modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .imePadding()
            .drawDropShadow(color = Color(0xFF4D5E6F))
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        val scrollState = rememberScrollState()
        val viewMaxHeight = maxHeight.value
        val columnMaxScroll = scrollState.maxValue
        val scrollStateValue = scrollState.value
        val paddingSize = (scrollStateValue * viewMaxHeight) / columnMaxScroll
        val animation = animateDpAsState(targetValue = paddingSize.dp)
        val scrollBarHeight = viewMaxHeight / list.size

        Column(
            Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            list.forEach { loc ->
                Text(
                    text = loc,
                    modifier = Modifier
                        .clickable { locationClick(loc) }
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(start = 16.dp)
                )
            }
        }
        if (scrollStateValue < columnMaxScroll) {
            Box(
                modifier = Modifier
                    .paddingFromBaseline(animation.value)
                    .padding(all = 4.dp)
                    .height(scrollBarHeight.dp)
                    .width(8.dp)
                    .background(
                        color = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
                    )
                    .align(Alignment.TopEnd),
            )
        }
    }
}