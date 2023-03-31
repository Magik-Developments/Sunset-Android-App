package com.madteam.sunset.design_system.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R

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
fun PasswordSecurityIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        PasswordIndicatorBox()
    }
}

@Composable
fun PasswordIndicatorBox() {
    Box(
        modifier = Modifier
            .size(height = 6.dp, width = 28.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = Color(0xFFD9D9D9))
    )
}

@Composable
fun PasswordTextField(
    passwordValue: String,
    onValueChange: (String) -> Unit,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
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
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
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
fun UsernameTextField(
    usernameValue: String,
    onValueChange: (String) -> Unit,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    DesignSystemTextField(
        value = usernameValue,
        onValueChange = onValueChange,
        hint = R.string.username,
        textType = KeyboardType.Text,
        endIcon = (endIcon)
    )
}

