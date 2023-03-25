package com.madteam.sunset.welcome.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.common.design_system.CustomSpacer
import com.madteam.sunset.common.design_system.MainTitle
import com.madteam.sunset.common.design_system.SubTitle
import com.madteam.sunset.design_system.common.EmailButton
import com.madteam.sunset.design_system.common.FacebookButton
import com.madteam.sunset.design_system.common.GoogleButton
import com.madteam.sunset.design_system.common.SunsetLogoImage

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SunsetLogoImage()
        CustomSpacer(56.dp)
        MainTitle()
        CustomSpacer(size = 8.dp)
        SubTitle(Modifier.align(Alignment.Start))
        CustomSpacer(size = 56.dp)
        EmailButton()
        CustomSpacer(size = 16.dp)
        GoogleButton()
        CustomSpacer(size = 16.dp)
        FacebookButton()
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
    WelcomeScreen()
}