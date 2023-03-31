@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.madteam.sunset.welcome.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.design_system.common.CustomSpacer
import com.madteam.sunset.design_system.common.EmailButton
import com.madteam.sunset.design_system.common.FacebookButton
import com.madteam.sunset.design_system.common.GoogleButton
import com.madteam.sunset.design_system.common.MainTitle
import com.madteam.sunset.design_system.common.SubTitle
import com.madteam.sunset.design_system.common.SunsetLogoImage
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onEmailClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
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
        EmailButton(onClick = onEmailClick)
        CustomSpacer(size = 16.dp)
        GoogleButton(onClick = onGoogleClick)
        CustomSpacer(size = 16.dp)
        FacebookButton(onClick = onFacebookClick)
    }
}

@Composable
fun ModalBottomSheetLayout() {
    val sheetState = rememberModalBottomSheetState(initialValue = Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        sheetContent = {
            BottomSheetSignIn()
        },
        sheetState = sheetState,
    ) {
        WelcomeScreen(
            onEmailClick = {
                scope.launch {
                    sheetState.animateTo(Expanded)
                }
            },
            onGoogleClick = { /*TODO*/ },
            onFacebookClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
    ModalBottomSheetLayout()
}