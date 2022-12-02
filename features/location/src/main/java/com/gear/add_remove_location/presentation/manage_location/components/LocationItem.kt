package com.gear.add_remove_location.presentation.manage_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Gray500
import com.gear.add_remove_location.presentation.ui.theme.LocationItemStyle

@Composable
fun LocationItem(
    imageRes: Int,
    location:String,
    bgColor: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .drawDropShadow(color = Color(0xFF4D5E6F))
            .background(bgColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(imageRes),
            contentDescription = "location",
            tint = Gray500,
            modifier = Modifier.padding(start = 2.dp)
        )

        Text(
            text = location,
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


@Preview(showBackground = true, backgroundColor = 0xffffffff)
@Composable
fun LocPrev() {
    LocationItem(location = "Lagos, Nigeria", imageRes = R.drawable.location_ic_on){}
}






