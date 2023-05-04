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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS

@Composable
fun VerifyAccountScreen(
  navController: NavController,
  viewModel: VerifyAccountViewModel = hiltViewModel(),
  pass: String,
) {

  val resendCounter by viewModel.resendCounter.collectAsStateWithLifecycle()
  val recheckCounter by viewModel.recheckCounter.collectAsStateWithLifecycle()
  val isVerified by viewModel.userVerified.collectAsStateWithLifecycle()

  VerifyAccountContent(
    credential = pass,
    resendText = resendCounter,
    recheckCounter = recheckCounter,
    isVerified = isVerified,
    resendButton = viewModel::sendVerifyEmailIntent,
    checkButton = viewModel::checkIfUserIsVerified,
    navigateTo = navController::navigate
  )
}

@Composable
fun VerifyAccountContent(
  credential: String,
  resendText: Int,
  recheckCounter: Int,
  isVerified: Boolean,
  resendButton: () -> Unit,
  checkButton: (String) -> Unit,
  navigateTo: (String) -> Unit
) {

  val mainAnimComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sunsetmountains))
  val successAnimComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.confetti))

  val resendColor = if (resendText > 0) {
    Color(0xFF999999)
  } else {
    Color(0xFFFFB600)
  }
  val title = if (isVerified) {
    string.welcome_aboard
  } else {
    string.almost_done
  }
  val subtitle = if (isVerified) {
    string.welcome_verify
  } else {
    string.verify_account_description
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CustomSpacer(size = 48.dp)
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .height(300.dp)
        .width(300.dp)
    ) {
      LottieAnimation(
        contentScale = ContentScale.Crop,
        composition = mainAnimComposition,
        iterations = LottieConstants.IterateForever
      )
      if (isVerified) {
        LottieAnimation(
          contentScale = ContentScale.Crop,
          composition = successAnimComposition,
          iterations = 1
        )
      }
    }
    CustomSpacer(size = 80.dp)
    Text(
      text = stringResource(title),
      style = primaryBoldHeadlineL,
      color = Color(0xFF333333)
    )
    CustomSpacer(size = 24.dp)
    Text(
      text = stringResource(subtitle),
      style = primaryBoldHeadlineS,
      color = Color(0xFF999999),
      textAlign = TextAlign.Center
    )
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (isVerified) {
        SmallButtonDark(onClick = {
          navigateTo(SunsetRoutes.MyProfileScreen.route)
        }, text = string.continue_text, enabled = true)
      } else {
        SmallButtonDark(onClick = {
          checkButton(credential)
        }, text = string.im_verified, enabled = recheckCounter == 0)
        CustomSpacer(size = 48.dp)
        Row() {
          Text(text = "Not arriving?", style = primaryMediumHeadlineS, color = Color(0xFF999999))
          CustomSpacer(size = 24.dp)
          Box(modifier = Modifier.clickable(onClick = { resendButton() })) {
            Text(text = "Resend", style = primaryMediumHeadlineS, color = resendColor)
          }
          CustomSpacer(size = 8.dp)
          if (resendText > 0) {
            Text(
              text = "($resendText s)",
              style = primaryMediumHeadlineS,
              color = Color(0xFF999999)
            )
          }
        }
      }
      CustomSpacer(size = 40.dp)
    }
  }
}