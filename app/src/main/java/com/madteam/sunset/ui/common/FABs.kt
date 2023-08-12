package com.madteam.sunset.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.madteam.sunset.ui.theme.SunsetTheme

@Composable
fun AddSpotFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFFFFB600),
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Default.AddLocationAlt, contentDescription = "")
    }
}

/*
PREVIEWS
 */

@Preview
@Composable
fun PreviewAddSpotFAB() {
    SunsetTheme {
        AddSpotFAB {}
    }
}