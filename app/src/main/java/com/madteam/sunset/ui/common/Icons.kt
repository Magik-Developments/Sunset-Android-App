package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R.string

@Composable
fun PasswordVisibilityOffIcon() {
  TextFieldIcon(
    icon = Icons.Outlined.Visibility,
    description = string.visibility_password_icon_description,
    iconTint = Color(0xFF333333)
  )
}

@Composable
fun SuccessIcon() {
  TextFieldIcon(
    icon = Icons.Outlined.CheckCircle,
    description = string.circle_check_icon_description,
    iconTint = Color(0xFF53A653)
  )
}

@Composable
fun ErrorIcon() {
  TextFieldIcon(
    icon = Icons.Outlined.Cancel,
    description = string.invalid_form_icon_description,
    iconTint = Color.Red
  )
}

@Composable
fun TextFieldIcon(
  icon: ImageVector,
  @StringRes description: Int,
  iconTint: Color
) {
  Icon(
    imageVector = icon,
    contentDescription = stringResource(id = description),
    tint = iconTint
  )
}

@Composable
fun MenuIconButton(
  onClick: () -> Unit
) {
  IconButton(onClick = onClick, modifier = Modifier.size(24.dp)) {
    Icon(
      imageVector = Icons.Outlined.Menu,
      contentDescription = "",
      modifier = Modifier.size(24.dp)
    )
  }
}

@Composable
fun CloseIconButton(
  onClick: () -> Unit
) {
  IconButton(onClick = onClick, modifier = Modifier.size(24.dp)) {
    Icon(
      imageVector = Icons.Outlined.Close,
      contentDescription = "",
      modifier = Modifier.size(24.dp)
    )
  }
}

@Composable
fun RoundedCloseIconButton(
  onClick: () -> Unit
) {
  IconButton(
    onClick = onClick, modifier = Modifier
      .size(24.dp)
      .background(Color(0x80000000), RoundedCornerShape(50.dp))
      .clip(
        RoundedCornerShape(50.dp)
      )
  ) {
    Icon(
      imageVector = Icons.Outlined.Close,
      contentDescription = "",
      modifier = Modifier.size(24.dp),
      tint = Color.White
    )
  }
}

