package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun EmailButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFB600),
            contentColor = Color(0xFFFFFFFF),
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(R.string.btn_continue_email),
            color = Color(0xFFFFFFFF),
            style = secondarySemiBoldHeadLineS
        )
    }
}

@Composable
fun GoogleButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
        ),
        contentPadding = PaddingValues(start = 16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF000000))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_google),
                contentDescription = "Logo Google",
                modifier = Modifier
                    .size(width = 24.dp, height = 24.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.btn_continue_google),
                color = Color(0xFF000000),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Composable
fun FacebookButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
        ),
        contentPadding = PaddingValues(start = 16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF000000))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_facebook),
                contentDescription = "Logo Google",
                modifier = Modifier
                    .size(width = 24.dp, height = 24.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.btn_continue_facebook),
                color = Color(0xFF000000),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Composable
fun SmallButtonDark(
    onClick: () -> Unit,
    @StringRes text: Int,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(150.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            disabledBackgroundColor = Color(0x80000000),
            disabledContentColor = Color(0x80FFFFFF)
        ),
        enabled = enabled

    ) {
        Text(
            text = stringResource(id = text),
            style = secondarySemiBoldHeadLineS,
            color = Color.White
        )
    }
}

@Composable
fun OtherLoginIconButtons(
    firstMethod: () -> Unit,
    secondMethod: () -> Unit
) {
    Row {
        IconButtonLight(
            buttonIcon = ImageVector.vectorResource(id = R.drawable.logo_google),
            description = R.string.google_icon_description,
            onClick = (firstMethod)
        )
        CustomSpacer(size = 24.dp)
        IconButtonLight(
            buttonIcon = Icons.Outlined.Mail,
            description = R.string.email_icon_description,
            onClick = (secondMethod)
        )
    }
}

@Composable
fun IconButtonLight(
    buttonIcon: ImageVector,
    @StringRes description: Int,
    iconTint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp, 24.dp),
            imageVector = buttonIcon,
            contentDescription = stringResource(description),
            tint = iconTint
        )
    }
}

@Composable
fun ThinButtonLight(
    onClick: () -> Unit,
    @StringRes text: Int,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(80.dp)
            .height(24.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            disabledBackgroundColor = Color(0x80000000),
            disabledContentColor = Color(0x80FFFFFF)
        ),
        enabled = enabled,
        contentPadding = PaddingValues(0.dp)

    ) {
        Text(
            text = stringResource(id = text),
            style = secondarySemiBoldBodyS,
            color = Color(0xFF333333)
        )
    }
}