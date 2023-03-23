package com.madteam.sunset.welcome.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun BottomSheetSignIn() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.8).dp),
            backgroundColor = Color(0xFFFFB600),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            CardContent()
        }
    }
}

@Composable
fun CardContent() {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 36.dp)
    ) {
        CustomSpacer(size = 8.dp)
        CardShape()
        CustomSpacer(size = 16.dp)
        CardTitle()
        CardSubtitle()
        CustomSpacer(size = 16.dp)
        EmailTextField(emailValue = emailValue, onValueChange = { emailValue = it })
        CustomSpacer(size = 16.dp)
        PasswordTextField(passwordValue = passwordValue, onValueChange = { passwordValue = it })
        CustomSpacer(size = 24.dp)
        SmallButtonDark(onClick = { /*TODO*/ }, text = string.sign_in)
        CustomSpacer(size = 16.dp)
        ForgotPasswordText()
        CustomSpacer(size = 40.dp)
        NotRegisteredSection()
    }
}

@Composable
fun CardShape() {
    Divider(
        color = Color(0xFFD9D9D9),
        thickness = 6.dp,
        modifier = Modifier
            .width(122.dp)
            .clip(RoundedCornerShape(50.dp))
    )
}

@Composable
fun NotRegisteredSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(0.5f)) {
            CustomDivider()
        }
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NotRegisteredText()
        }
        Column(Modifier.weight(0.5f)) {
            CustomDivider()
        }
    }
    Column {
        CustomSpacer(size = 24.dp)
        Row() {
            IconButtonLight(ImageVector.Companion.vectorResource(id = R.drawable.logo_google), string.google_icon_description ,onClick = { /* TODO */ })
            CustomSpacer(size = 24.dp)
            IconButtonLight(Icons.Outlined.Mail, string.email_icon_description, onClick = {/* TODO */})
        }
    }
}

@Composable
fun IconButtonLight(
    buttonIcon: ImageVector,
    @StringRes description: Int,
    iconTint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp))
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
fun NotRegisteredText() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(string.not_registered_yet),
            style = secondarySemiBoldBodyM,
            color = Color(0xFF666666)
        )
        Text(
            text = stringResource(string.sign_up_with),
            style = secondarySemiBoldBodyM,
            color = Color(0xFF666666)
        )
    }
}

@Composable
fun CustomDivider() {
    Divider(
        color = Color(0xFF999999),
    )
}

@Composable
fun ForgotPasswordText() {
    Text(
        text = stringResource(string.forgot_your_password),
        style = secondarySemiBoldBodyM,
        color = Color(0xFF333333)
    )
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
fun PasswordTextField(
    passwordValue: String,
    onValueChange: (String) -> Unit
) {
    DesignSystemTextField(
        value = passwordValue,
        onValueChange = onValueChange,
        hint = string.password,
        textType = KeyboardType.Password,
        endIcon = {
            TextFieldIcon(
                icon = Icons.Outlined.Visibility,
                description = string.visibility_password_icon_description,
                iconTint = Color(0xFF333333)
            )
        }
    )
}

@Composable
fun EmailTextField(
    emailValue: String,
    onValueChange: (String) -> Unit
) {
    DesignSystemTextField(
        value = emailValue,
        onValueChange = onValueChange,
        hint = string.email_address,
        textType = KeyboardType.Email
    )
}

@Composable
fun DesignSystemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes hint: Int,
    textType: KeyboardType,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
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
fun CardTitle() {
    Text(
        text = stringResource(id = string.welcome_back),
        style = secondarySemiBoldHeadLineM
    )
}

@Composable
fun CardSubtitle() {
    Text(
        text = stringResource(id = string.enter_details_below),
        style = secondaryRegularBodyL
    )
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignInPreview() {
    BottomSheetSignIn()
}