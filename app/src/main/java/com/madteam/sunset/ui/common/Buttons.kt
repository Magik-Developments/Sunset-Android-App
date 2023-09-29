package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessHigh
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.SunsetTheme
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun SunsetButton(onClick: () -> Unit) {
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

@Preview
@Composable
fun PreviewEmailButton() {
    SunsetTheme { SunsetButton {} }
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

@Preview
@Composable
fun PreviewGoogleButton() {
    SunsetTheme { GoogleButton {} }
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

@Preview
@Composable
fun PreviewFacebookButton() {
    SunsetTheme { FacebookButton {} }
}

@Composable
fun SmallButtonDark(
    onClick: () -> Unit, @StringRes text: Int, enabled: Boolean
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
fun SmallButtonSunset(
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
            backgroundColor = Color(0xFFFFB600),
            disabledBackgroundColor = Color(0xFFFFE094),
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

@Preview
@Composable
fun PreviewSmallButtonDark() {
    SunsetTheme {
        SmallButtonDark(
            onClick = { }, text = R.string.save, true
        )
    }
}

@Composable
fun OtherLoginIconButtons(
    firstMethod: () -> Unit, secondMethod: () -> Unit
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

@Preview
@Composable
fun PreviewOtherLoginIconButtons() {
    SunsetTheme {
        OtherLoginIconButtons(firstMethod = { }, secondMethod = {})
    }
}

@Composable
fun IconButtonLight(
    buttonIcon: ImageVector,
    @StringRes description: Int,
    onClick: () -> Unit,
    iconTint: Color = Color.Unspecified
) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp, 24.dp),
            imageVector = buttonIcon,
            contentDescription = stringResource(description),
            tint = iconTint
        )
    }
}

@Preview
@Composable
fun PreviewIconButtonLight() {
    SunsetTheme {
        IconButtonLight(
            buttonIcon = ImageVector.vectorResource(id = R.drawable.logo_google),
            description = R.string.google_icon_description,
            onClick = {}
        )
    }
}

@Composable
fun IconButtonDark(
    buttonIcon: ImageVector,
    @StringRes description: Int,
    onClick: () -> Unit,
    iconTint: Color = Color.Unspecified,
) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp, 24.dp),
            imageVector = buttonIcon,
            contentDescription = stringResource(description),
            tint = iconTint
        )
    }
}

@Preview
@Composable
fun PreviewIconButtonDark() {
    SunsetTheme {
        IconButtonDark(
            buttonIcon = ImageVector.vectorResource(id = R.drawable.logo_google),
            description = R.string.google_icon_description,
            onClick = {}
        )
    }
}

@Composable
fun ThinButtonLight(
    onClick: () -> Unit, @StringRes text: Int, enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(88.dp)
            .height(32.dp),
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
            style = secondarySemiBoldBodyM,
            color = Color(0xFF333333)
        )
    }
}

@Preview
@Composable
fun PreviewThinButtonLight() {
    SunsetTheme {
        ThinButtonLight(
            onClick = {},
            R.string.edit_profile
        )
    }
}

@Composable
fun LargeLightButton(
    onClick: () -> Unit, @StringRes text: Int, modifier: Modifier = Modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick, modifier = modifier.height(48.dp), colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFFFFF),
        ), shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Color(0xFF000000))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(text),
                color = Color(0xFF000000),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Composable
fun LargeDangerButton(
    onClick: () -> Unit,
    @StringRes text: Int,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFF4444),
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFFF4444))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(text),
                color = Color.White,
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Preview
@Composable
fun PreviewLargeLightButton() {
    SunsetTheme {
        LargeLightButton(
            onClick = {},
            R.string.show_all_reviews
        )
    }
}

@Preview
@Composable
fun PreviewLargeDangerButton() {
    SunsetTheme {
        LargeDangerButton(
            onClick = {},
            R.string.delete_spot
        )
    }
}

@Composable
fun LargeDarkButton(
    onClick: () -> Unit, @StringRes text: Int, modifier: Modifier = Modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick, modifier = modifier.height(48.dp), colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF000000),
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(text),
                color = Color(0xFFFFFFFF),
                style = secondarySemiBoldHeadLineS
            )
        }
    }
}

@Composable
fun SmallSunsetButton(
    onClick: () -> Unit,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFFB600)
        ),
        modifier = modifier
            .height(30.dp)
            .width(120.dp)
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = text),
                style = secondarySemiBoldBodyS,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
fun FilterScoreButton(
    filterOptions: List<Int>,
    selectedOption: Int,
    onOptionClicked: (Int) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(filterOptions) { _, option ->
            val isSelected = selectedOption == option
            val customBackgroundColor = if (isSelected) Color(0xFFFFE094) else Color.White
            val customBorderColor = if (isSelected) Color(0xFFFFB600) else Color(0xFF999999)
            val customTextColor = if (isSelected) Color.Black else Color(0xFF333333)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, customBorderColor, RoundedCornerShape(10.dp))
                    .background(customBackgroundColor, RoundedCornerShape(10.dp))
                    .clickable { onOptionClicked(option) }

            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "> $option",
                        style = secondarySemiBoldBodyM,
                        color = customTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    CustomSpacer(size = 4.dp)
                    Icon(
                        imageVector = Icons.Outlined.BrightnessHigh,
                        contentDescription = "",
                        modifier = Modifier
                            .size(16.dp),
                        tint = customTextColor
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun FilterScoreButtonPreview() {
    FilterScoreButton(
        filterOptions = listOf(4, 6, 8),
        selectedOption = 6,
        onOptionClicked = {}
    )
}

@Preview
@Composable
fun SmallSunsetButtonPreview(

) {
    SmallSunsetButton(onClick = { /*TODO*/ }, text = R.string.explore_spots)
}

@Preview
@Composable
fun PreviewLargeDarkButton() {
    SunsetTheme {
        LargeDarkButton(
            onClick = {},
            R.string.show_all_reviews
        )
    }
}