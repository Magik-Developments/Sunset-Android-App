package com.madteam.sunset.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.primaryBoldDisplayM

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SunsetLogoImage()
        CustomSpacer(70.dp)
        MainTitle()
    }
}

@Composable
fun SunsetLogoImage() {
    Image(
        modifier = Modifier.size(width = 214.dp, height = 197.dp),
        painter = painterResource(id = R.drawable.logo_degrade),
        contentDescription = "Sunset logo degrade"
    )
}

@Composable
fun CustomSpacer(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}

@Composable
fun MainTitle() {
    Column {
        Text(
            text = stringResource(string.hello),
            style = primaryBoldDisplayM
        )
        Text(
            text = stringResource(string.we_are_sunset),
            style = primaryBoldDisplayM
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreenPrev() {
    WelcomeScreen()
}