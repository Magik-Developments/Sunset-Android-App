package com.madteam.sunset.common.design_system

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.*
import com.madteam.sunset.welcome.ui.CARD_HEIGHT

//Logos

@Composable
fun SunsetLogoImage() {
    Image(
        modifier = Modifier.size(width = 214.dp, height = 197.dp),
        painter = painterResource(id = R.drawable.logo_degrade),
        contentDescription = "Sunset logo degrade"
    )
}


// Spacers

@Composable
fun CustomSpacer(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}

@Composable
fun CustomDivider(
    modifier: Modifier,
    height: Dp = 1.dp,
    color: Color
) {
    Divider(
        color = color,
        modifier = modifier,
        thickness = height
    )
}

// Welcome Screen

@Composable
fun MainTitle() {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.hello),
            style = primaryBoldDisplayM
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.we_are_sunset),
            style = primaryBoldDisplayM
        )
    }
}

@Composable
fun SubTitle(modifier: Modifier) {
    Text(
        text = stringResource(R.string.welcome_subtitle),
        style = secondaryRegularBodyL,
        modifier = modifier
    )
}


//Sign In / Sign Up Card

@Composable
fun CardHandler() {
    Divider(
        color = Color(0xFFD9D9D9),
        thickness = 6.dp,
        modifier = Modifier
            .width(122.dp)
            .clip(RoundedCornerShape(50.dp))
    )
}

@Composable
fun CardShade() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT + 8).dp)
            .width((LocalConfiguration.current.screenWidthDp * 0.8).dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color(0xFFFFe094))
    )
}

@Composable
fun CardTitle(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        style = secondarySemiBoldHeadLineM
    )
}

@Composable
fun CardSubtitle(text: Int) {
    Text(
        text = stringResource(id = text),
        style = secondaryRegularBodyL
    )
}

@Composable
fun ForgotPasswordText() {
    Text(
        text = stringResource(R.string.forgot_your_password),
        style = secondarySemiBoldBodyM,
        color = Color(0xFF333333)
    )
}

@Composable
fun OtherLoginMethodsText(modifier: Modifier, @StringRes text: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = text),
            style = secondarySemiBoldBodyM,
            color = Color(0xFF666666),
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtherLoginMethodsSection(@StringRes text: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomDivider(Modifier.weight(0.5f), color = Color(0xFFd9d9d9))
        OtherLoginMethodsText(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            text = text
        )
        CustomDivider(Modifier.weight(0.5f), color = Color(0xFFd9d9d9))
    }
}

//Text Fields

@Composable
fun DesignSystemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes hint: Int,
    textType: KeyboardType,
    endIcon: @Composable () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .border(1.dp, Color(0xFF999999), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color(0xFF333333),
            cursorColor = Color.Black,
            focusedLabelColor = Color(0xFF999999),
            unfocusedLabelColor = Color(0xFF999999),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        label = { Text(text = stringResource(hint)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = textType),
        trailingIcon = endIcon
    )
}

@Composable
fun PasswordTextField(
    passwordValue: String,
    onValueChange: (String) -> Unit,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp))}
) {
    DesignSystemTextField(
        value = passwordValue,
        onValueChange = onValueChange,
        hint = R.string.password,
        textType = KeyboardType.Password,
        endIcon = (endIcon)
    )
}

@Composable
fun EmailTextField(
    emailValue: String,
    onValueChange: (String) -> Unit,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp))}
) {
    DesignSystemTextField(
        value = emailValue,
        onValueChange = onValueChange,
        hint = R.string.email_address,
        textType = KeyboardType.Email,
        endIcon = (endIcon)
    )
}

@Composable
fun NameTextField(
    nameValue: String,
    onValueChange: (String) -> Unit,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    DesignSystemTextField(
        value = nameValue,
        onValueChange = onValueChange,
        hint = R.string.name,
        textType = KeyboardType.Text,
        endIcon = (endIcon)
    )
}

// Buttons

@Composable
fun EmailButton() {
    Button(
        onClick = { /*TODO*/ },
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
fun GoogleButton() {
    Button(
        onClick = { /*TODO*/ },
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
fun FacebookButton() {
    Button(
        onClick = { /*TODO*/ },
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
    @StringRes text: Int
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(150.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)

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
            buttonIcon = ImageVector.Companion.vectorResource(id = R.drawable.logo_google),
            description = R.string.google_icon_description,
            onClick = (firstMethod))
        CustomSpacer(size = 24.dp)
        IconButtonLight(
            buttonIcon = Icons.Outlined.Mail,
            description = R.string.email_icon_description,
            onClick = (secondMethod))
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

// Icon

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

