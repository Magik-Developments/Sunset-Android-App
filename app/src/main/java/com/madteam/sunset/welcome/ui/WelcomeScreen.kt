package com.madteam.sunset.welcome.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

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
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(string.hello),
            style = primaryBoldDisplayM
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(string.we_are_sunset),
            style = primaryBoldDisplayM
        )
    }
}

@Composable
fun SubTitle(modifier: Modifier) {
    Text(
        text = stringResource(string.welcome_subtitle),
        style = secondaryRegularBodyL,
        modifier = modifier
    )
}

@Composable
fun EmailButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFB600),
            contentColor = Color(0xFFFFFFFF),
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(string.btn_continue_email),
            color = Color(0xFFFFFFFF),
            style = secondarySemiBoldHeadLineS
        )
    }
}

@Composable
fun GoogleButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
        ),
        contentPadding = PaddingValues(start = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF000000))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_google),
                contentDescription = "Logo Google",
                modifier = Modifier
                    .size(width = 27.84.dp, height = 30.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(string.btn_continue_google),
                color = Color(0xFF000000),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Composable
fun FacebookButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
        ),
        contentPadding = PaddingValues(start = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF000000))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_facebook),
                contentDescription = "Logo Google",
                modifier = Modifier
                    .size(width = 27.84.dp, height = 30.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(string.btn_continue_facebook),
                color = Color(0xFF000000),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_4)
@Composable
fun WelcomeScreenPrev() {
    WelcomeScreen()
}