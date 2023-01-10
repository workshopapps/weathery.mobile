package com.gear.add_remove_location.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.gear.add_remove_location.R

val Outfit = FontFamily(
    Font(R.font.location_outfit_regular)
)

val LocationItemStyle = TextStyle(
    fontFamily = Outfit,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start,
    fontSize = 16.sp,
    letterSpacing = 0.sp
)

val LocationSubStyle = TextStyle(
    fontFamily = Outfit,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Start,
    fontSize = 24.sp,
    letterSpacing = 0.sp
)

val LocationTitleStyle = TextStyle(
    fontFamily = Outfit,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center,
    fontSize = 24.sp,
    letterSpacing = 0.01.em
)

val ButtonTextStyle = TextStyle(
    fontFamily = Outfit,
    lineHeight = 24.sp,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center,
    fontSize = 14.sp,
    letterSpacing = 0.em
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = Outfit
)