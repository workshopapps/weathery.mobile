package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.presentation.ui.theme.Primary500


@Composable
fun LearnMore(
    learnMore: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.learnmore),
            fontFamily = Outfit,
            fontSize = 16.sp
        )
        Text(text = stringResource(R.string.weatherreport),
            fontFamily = Outfit,
            fontSize = 16.sp,
            color = Primary500,
            modifier = Modifier.clickable { learnMore() })
    }
}

@Preview(showBackground = true)
@Composable
fun LearnPrev() {
    LearnMore {}
}