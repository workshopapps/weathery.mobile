package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Outfit
import com.gear.add_remove_location.presentation.ui.theme.Primary500

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Primary500,
            backgroundColor = Color.Transparent
        ),
        value = text,
        onValueChange = {
            onSearch(it)
        },
        leadingIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.location_ic_search_icon),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colors.primary
                )
            }

        },
        placeholder = {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.choosealocation),
                    fontFamily = Outfit,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
    )
}
