package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.LocationItemStyle

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    imageRes: Int,
    location:String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(imageRes),
            contentDescription = stringResource(id = R.string.location),
            modifier = Modifier.padding(start = 2.dp)
        )

        Text(
            text = location.capitalize(Locale.current),
            Modifier.padding(start = 10.dp),
            style = LocationItemStyle
        )
    }
}

fun Modifier.drawDropShadow(
    color: Color,
    alpha: Float = 0.25f,
    borderRadius: Dp = 8.dp,
    shadowRadius: Dp = 0.4.dp,
    offsetY: Dp = 0.8.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}
