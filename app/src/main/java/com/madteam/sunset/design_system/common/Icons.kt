package com.madteam.sunset.design_system.common

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.madteam.sunset.R

@Composable
fun PasswordVisibilityOffIcon() {
    TextFieldIcon(
        icon = Icons.Outlined.Visibility,
        description = R.string.visibility_password_icon_description,
        iconTint = Color(0xFF333333)
    )
}

@Composable
fun SuccessIcon() {
    TextFieldIcon(
        icon = Icons.Outlined.CheckCircle,
        description = R.string.circle_check_icon_description,
        iconTint = Color(0xFF53A653)
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

