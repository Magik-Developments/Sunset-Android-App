package com.madteam.sunset.ui.screens.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madteam.sunset.BuildConfig
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.SunsetLogoImage
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL

private const val INSTAGRAM_URL = "https://instagram.com/sunsetappofficial?igshid=N2RpanJhcTA0bXNv"
private const val TIKTOK_URL = "https://www.tiktok.com/@sunsetappofficial?_t=8hNQPtDXz8L&_r=1"
private const val WEB_URL = "https://www.sunsetapp.es"
private const val GITHUB_URL = "https://github.com/Mad-Development-Team/Sunset-Android-App"

@Composable
fun AboutScreen(
    navController: NavController,
) {

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.about_us) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            AboutContent()
        }
    }

}

@Composable
fun AboutContent() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.sunset),
            style = primaryBoldHeadlineL
        )
        Text(
            text = "v" + BuildConfig.VERSION_NAME,
            style = secondaryRegularBodyL,
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 16.dp)
        SunsetLogoImage()
        CustomSpacer(size = 16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                uriHandler.openUri(INSTAGRAM_URL)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = stringResource(id = R.string.go_to_instagram)
                )
            }
            IconButton(onClick = {
                uriHandler.openUri(TIKTOK_URL)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tiktok),
                    contentDescription = stringResource(id = R.string.go_to_tiktok)
                )
            }
            IconButton(onClick = {
                uriHandler.openUri(WEB_URL)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_web),
                    contentDescription = stringResource(id = R.string.go_to_web)
                )
            }
            IconButton(onClick = {
                uriHandler.openUri(GITHUB_URL)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = stringResource(id = R.string.go_to_github)
                )
            }
        }
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.made_with_love_from_bcn),
            style = primaryBoldHeadlineS,
            textAlign = TextAlign.Center
        )
    }

}