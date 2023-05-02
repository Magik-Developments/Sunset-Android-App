package com.madteam.sunset.ui.screens.verifyaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS

@Composable
fun LostPasswordScreen(
  viewModel: VerifyAccountViewModel = hiltViewModel(),
  navController: NavController
) {

  val resendCounter by viewModel.resendCounter.collectAsStateWithLifecycle()

  LostPasswordContent(
    resendText = resendCounter,
    resendButton = viewModel::sendVerifyEmailIntent
  )
}

@Composable
fun LostPasswordContent(
  sendButton: (String) -> Unit = { },
  validateForm: (String) -> Unit = { },
  navigateTo: (String) -> Unit = { },
  resendText: Int,
  resendButton: () -> Unit
) {

  val mainAnimComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sunsetmountains))
  val successAnimComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sunsetlighthouse))

  var emailValueText by remember { mutableStateOf("") }
  var emailSent by remember { mutableStateOf(false) }

  val resendColor = if (resendText > 0) { Color(0xFF999999) } else { Color(0xFFFFB600) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CustomSpacer(size = 48.dp)
    LottieAnimation(
      modifier = Modifier
        .height(250.dp)
        .width(250.dp),
      composition = mainAnimComposition,
      iterations = LottieConstants.IterateForever
    )
    CustomSpacer(size = 80.dp)
    Text(
      text = stringResource(string.almost_done),
      style = primaryBoldHeadlineL,
      color = Color(0xFF333333)
    )
    CustomSpacer(size = 24.dp)
    Text(
      text = stringResource(string.verify_account_description),
      style = primaryBoldHeadlineS,
      color = Color(0xFF999999),
      textAlign = TextAlign.Center
    )
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      SmallButtonDark(onClick = {
        sendButton(emailValueText)
        emailSent = true
      }, text = string.im_verified, enabled = isValidForm && !emailSent)
      CustomSpacer(size = 48.dp)
      Row() {
        Text(text = "Not arriving?", style = primaryMediumHeadlineS, color = Color(0xFF999999))
        CustomSpacer(size = 24.dp)
        Box(modifier = Modifier.clickable(onClick = { resendButton() })) {
          Text(text = "Resend", style = primaryMediumHeadlineS, color = resendColor)
        }
        CustomSpacer(size = 8.dp)
        if (resendText > 0) {
          Text(text = "($resendText s)", style = primaryMediumHeadlineS, color = Color(0xFF999999))
        }
      }
      CustomSpacer(size = 40.dp)
    }
  }
}