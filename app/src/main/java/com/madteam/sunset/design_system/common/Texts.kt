package com.madteam.sunset.design_system.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS

@Composable
fun UserUsernameText(username: String) {
  Text(
    text = username,
    style = primaryBoldHeadlineL,
    color = Color.Black
  )
}

@Composable
fun UserLocationText(location: String) {
  Text(
    text = location,
    style = secondaryRegularHeadlineS,
    color = Color(0xFF333333)
  )
}