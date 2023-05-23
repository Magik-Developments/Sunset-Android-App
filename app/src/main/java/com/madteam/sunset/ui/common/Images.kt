package com.madteam.sunset.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyS

@Composable
fun ProfileImage(
    @DrawableRes image: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = stringResource(string.profile_user_image_description),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    )
}

@Composable
fun ImageSliderCounter(
    actualImage: Int,
    totalImages: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0x80FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .height(18.dp)
            .width(40.dp), contentAlignment = Alignment.Center
    ) {
        Row() {
            Text(text = actualImage.toString(), style = secondarySemiBoldBodyS, color = Color.White)
            Text(text = " / ", style = secondarySemiBoldBodyS, color = Color.White)
            Text(text = totalImages.toString(), style = secondarySemiBoldBodyS, color = Color.White)
        }

    }
}
