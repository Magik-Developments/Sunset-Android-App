package com.madteam.sunset.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun ProfilePostTypeTab(
    tabOptions: List<String>,
    selectedTab: Int,
    tabOnClick: (Int) -> Unit
) {

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color(0xFFFFB600),
        modifier = Modifier
            .padding(8.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(60)),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        },
        divider = {
            Box {}
        }
    ) {
        tabOptions.forEachIndexed { index, text ->
            val selected = selectedTab == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(30.dp)
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color(0xFFFFB600)
                    ),
                selected = selected,
                onClick = { tabOnClick(index) },
                text = {
                    Text(
                        text = text,
                        color = if (selected) {
                            Color(0xFFFFB600)
                        } else {
                            Color.White
                        },
                        style = secondarySemiBoldHeadLineS
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ProfilePostTypeTabPreview() {
    ProfilePostTypeTab(
        tabOptions = listOf("Posts", "Spots", "Saved"),
        selectedTab = 0,
        tabOnClick = {}
    )
}