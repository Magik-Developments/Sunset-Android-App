package com.madteam.sunset.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun UserLocationText(location: String) {
  Text(
    text = location,
    style = secondaryRegularHeadlineS,
    color = Color(0xFF333333)
  )
}

@Composable
fun UserNameText(userName: String) {
  Text(
    text = userName,
    style = secondarySemiBoldHeadLineS,
    color = Color(0xFF333333)
  )
}

@Composable
fun FollowsUserStates(){
  // TODO: Do this component properly
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(text = "340", style = secondarySemiBoldBodyL)
    CustomSpacer(size = 8.dp)
    Text(text = "Following", style = secondaryRegularBodyL)
    CustomSpacer(size = 24.dp)
    Text(text = "87", style = secondarySemiBoldBodyL)
    CustomSpacer(size = 8.dp)
    Text(text = "Followers", style = secondaryRegularBodyL)
  }
}