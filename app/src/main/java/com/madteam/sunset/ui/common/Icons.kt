package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.madteam.sunset.R
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

