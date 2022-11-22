package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Gray700
import com.gear.add_remove_location.presentation.ui.theme.Gray900
import com.gear.add_remove_location.presentation.ui.theme.Outfit


@Composable
fun WrongSearchDialog(wrongSearch: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.location_ic_search_icon),//Change to actual res
            contentDescription = "No results"
        )
        Text(
            text = "No Results",
            fontFamily = Outfit,
            color = Gray900,
            fontSize = 32.sp,
            fontWeight = FontWeight.W600
        )
        Text(
            text = "No results found for \"$wrongSearch\" .",
            fontFamily = Outfit,
            color = Gray700,
            fontSize = 24.sp,
            fontWeight = FontWeight.W600
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WrongSearchCompPrev() {
    WrongSearchDialog(wrongSearch = "Dr see")
}