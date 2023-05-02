package com.madteam.sunset.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.utils.shadow

@Composable
fun SunsetBottomNavigation() {

    val unselectedContentColor = Color(0xB3FFB600)
    val selectedContentColor = Color(0xFF000000)

    var selected by remember { mutableStateOf(4) }
    BottomNavigation(
        modifier = Modifier
            .height(84.dp)
            .shadow(
                color = Color(0x33000000),
                blurRadius = 2.dp,
                offsetY = (-2).dp
            )
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
        backgroundColor = Color.White
    ) {
        BottomNavigationItem(selected = selected == 0, onClick = { selected = 0 }, icon = {
            Icon(
                imageVector = Icons.Filled.Home, contentDescription = "Home Icon",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = selectedContentColor)
        BottomNavigationItem(selected = selected == 1, onClick = { selected = 1 }, icon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = selectedContentColor)
        BottomNavigationItem(selected = selected == 2, onClick = { selected = 2 }, icon = {
            Icon(
                imageVector = Icons.Default.AddCircle, contentDescription = "Add Circle Icon",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = selectedContentColor)
        BottomNavigationItem(selected = selected == 3, onClick = { selected = 3 }, icon = {
            Icon(
                imageVector = Icons.Default.Favorite, contentDescription = "Favorite Icon",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = selectedContentColor)
        BottomNavigationItem(selected = selected == 4, onClick = { selected = 4 }, icon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = selectedContentColor)
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    SunsetBottomNavigation()
}