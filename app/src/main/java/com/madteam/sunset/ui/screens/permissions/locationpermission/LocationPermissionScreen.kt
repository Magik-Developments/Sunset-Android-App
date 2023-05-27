package com.madteam.sunset.ui.screens.permissions.locationpermission

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.LargeDarkButton
import com.madteam.sunset.ui.common.RoundedCloseIconButton
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS

@Composable
fun LocationPermissionScreen(

) {
    LocationPermissionContent()
}

@Composable
fun LocationPermissionContent() {

    val animComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.bouncing_earth))

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = constraints.maxHeight / 3
            val halfWidth = constraints.maxWidth / 2
            val centerY = constraints.maxHeight.toFloat()
            val arcRadius = radius * 2
            Rect(
                (halfWidth - radius).toFloat(), centerY - radius,
                (halfWidth + radius).toFloat(), centerY + radius
            )
            drawArc(
                color = Color(0x33FFA500),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                size = Size(arcRadius.toFloat(), arcRadius.toFloat()),
                topLeft = Offset(halfWidth.toFloat() - radius, centerY - radius)
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSpacer(size = 16.dp)
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                RoundedCloseIconButton(onClick = {})
            }
            CustomSpacer(size = 48.dp)
            LottieAnimation(
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp),
                composition = animComposition,
                iterations = LottieConstants.IterateForever
            )
            CustomSpacer(size = 80.dp)
            Text(text = "Enable location", style = primaryBoldHeadlineL, color = Color(0xFF333333))
            CustomSpacer(size = 24.dp)
            Text(
                text = "There are many beautiful sunsets\n" +
                        "around the world...",
                style = primaryMediumHeadlineS,
                color = Color(0xFF999999),
                textAlign = TextAlign.Center
            )
            Text(
                text = "but we want to show you the\n" +
                        "ones closest to you",
                style = primaryMediumHeadlineS,
                color = Color(0xFF999999),
                textAlign = TextAlign.Center
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LargeDarkButton(onClick = { /*TODO*/ }, text = R.string.enable_location)
                CustomSpacer(size = 60.dp)
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun LocationPermissionPreview() {
    LocationPermissionContent()
}