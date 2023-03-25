package com.madteam.sunset.design_system.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.madteam.sunset.common.design_system.CustomSpacer

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
fun PasswordSecurityIndicator(passwordValue: String) {
    val passwordSecurity = evaluatePasswordSecurity(passwordValue)
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
       repeat(3) { level ->
           val indicatorLevel = when(passwordSecurity){
               PasswordStrength.WEAK -> 1
               PasswordStrength.MEDIUM -> if (level < 2) 2 else 1
               PasswordStrength.STRONG -> if (level < 2) 2 else 1
               PasswordStrength.VERY_STRONG -> if (level < 2) 2 else 1
           }
           PasswordIndicatorBox(level = indicatorLevel)
           if (level < 2) {
               CustomSpacer(size = 6.dp)
           }
       }
    }
}

enum class PasswordStrength {
    WEAK, MEDIUM, STRONG, VERY_STRONG
}

fun evaluatePasswordSecurity(passwordValue: String): PasswordStrength {
    val hasLowerCase = passwordValue.matches(Regex(".*[a-z].*"))
    val hasUpperCase = passwordValue.matches(Regex(".*[A-Z].*"))
    val hasDigit = passwordValue.matches(Regex(".*\\d.*"))
    val hasSpecialChar = passwordValue.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))

    return when {
        passwordValue.isBlank() -> PasswordStrength.WEAK
        passwordValue.length < 8 || !hasLowerCase || !hasUpperCase || !hasDigit ->
            PasswordStrength.MEDIUM
        passwordValue.length < 12 || (!hasSpecialChar && hasDigit) ->
            PasswordStrength.STRONG
        else ->
            PasswordStrength.VERY_STRONG
    }
}

@Composable
fun PasswordIndicatorBox(level: Int) {
    val color = when (level) {
        1 -> Color(0xFFD9D9D9)
        2 -> Color(0xFF53A653)
        else -> Color(0xFFD9D9D9)
    }
    Box(modifier = Modifier
        .size(height = 6.dp, width = 28.dp)
        .clip(RoundedCornerShape(50.dp))
        .background(color = color))
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

