package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.SunsetTheme

@Composable
fun PasswordVisibilityOffIcon() {
    TextFieldIcon(
        icon = Icons.Outlined.Visibility,
        description = string.visibility_password_icon_description,
        iconTint = Color(0xFF333333)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordVisibilityOffIcon() {
    SunsetTheme {
        PasswordVisibilityOffIcon()
    }
}

@Composable
fun SuccessIcon() {
    TextFieldIcon(
        icon = Icons.Outlined.CheckCircle,
        description = string.circle_check_icon_description,
        iconTint = Color(0xFF53A653)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSuccessIcon() {
    SunsetTheme {
        SuccessIcon()
    }
}

@Composable
fun ErrorIcon() {
    TextFieldIcon(
        icon = Icons.Outlined.Cancel,
        description = string.invalid_form_icon_description,
        iconTint = Color.Red
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorIcon() {
    SunsetTheme {
        ErrorIcon()
    }
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(24.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "",
            modifier = Modifier.size(16.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun RoundedLightBackButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(48.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun RoundedLightSaveButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isSaved: Boolean = false
) {
    val iconStyle = if (isSaved) {
        Icons.Filled.Bookmark
    } else {
        Icons.Outlined.Bookmark
    }
    IconButton(
        onClick = onClick, modifier = modifier
            .size(48.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = iconStyle,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun RoundedLightSendButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(48.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_send),
            contentDescription = "",
            modifier = Modifier.size(36.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun RoundedLightEditButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(48.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun RoundedLightReportButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(48.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Flag,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}


@Composable
fun RoundedLightLikeButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isLiked: Boolean
) {
    val iconStyle = if (isLiked) {
        Icons.Filled.Favorite
    } else {
        Icons.Outlined.Favorite
    }
    val iconColor = if (isLiked) {
        Color(0xFFFD0553)
    } else {
        Color.Black
    }

    IconButton(
        onClick = onClick, modifier = modifier
            .height(46.dp)
            .width(66.dp)
            .background(Color.White, RoundedCornerShape(50.dp))
            .clip(
                RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            imageVector = iconStyle,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )
    }
}

@Preview
@Composable
fun PreviewRoundedLightReportButton() {
    RoundedLightReportButton(onClick = { /*TODO*/ }, modifier = Modifier)
}

@Preview
@Composable
fun PreviewRoundedLightEditButton() {
    RoundedLightEditButton(onClick = { /*TODO*/ }, modifier = Modifier)
}

@Preview
@Composable
fun PreviewRoundedLightLikeButton() {
    RoundedLightLikeButton(onClick = { /*TODO*/ }, modifier = Modifier, isLiked = false)
}

