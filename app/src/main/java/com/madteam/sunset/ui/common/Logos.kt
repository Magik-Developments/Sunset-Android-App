package com.madteam.sunset.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R

@Composable
fun SunsetLogoImage() {
    Image(
        modifier = Modifier.size(width = 214.dp, height = 197.dp),
        painter = painterResource(id = R.drawable.logo_degrade),
        contentDescription = "Sunset logo degrade"
    )
}

