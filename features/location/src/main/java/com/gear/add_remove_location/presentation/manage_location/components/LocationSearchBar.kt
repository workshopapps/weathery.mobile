package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.presentation.ui.theme.Gray800
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Primary500

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LocationSearchBar(getText:(String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Primary500),
        value = text,
        onValueChange = {
            text = it
            getText(it)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.location_ic_search_icon),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colors.primary
            )
        },
        placeholder = {
            Box (Modifier.fillMaxSize()){
                Text(
                    text = "Choose a location",
                    fontFamily = Outfit,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        },
        trailingIcon = {
            //Set visibility to focus change
            //Add trailing unit to show Cancel
            if(text.isNotBlank()){
                Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { text = "" }
            )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {keyboardController?.hide()}),
        modifier = Modifier
            .padding(24.dp)
            .height(56.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LocSearchPrev() {
    LocationSearchBar{}
}