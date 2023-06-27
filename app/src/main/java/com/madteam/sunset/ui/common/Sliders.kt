package com.madteam.sunset.ui.common

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScoreSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        steps = 10,
        colors = SliderDefaults.colors(
            thumbColor = Color(0xFFFFB600),
            activeTrackColor = Color(0xFFFFB600)
        ),
        valueRange = 0.0f..10.0f
    )
}

@Preview
@Composable
fun ScoreSliderPreview() {
    val sliderValue by remember { mutableStateOf(5.0f) }
    ScoreSlider(value = sliderValue) {}
}