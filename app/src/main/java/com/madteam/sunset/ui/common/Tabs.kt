package com.madteam.sunset.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        containerColor = Color(0xFFFFB600),
        divider = {},
        indicator = { tabPositions: List<TabPosition> ->
            PrimaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                width = tabPositions[selectedTabIndex].width / 2,
                height = 5.dp,
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(50)
            )
        },
        tabs = {
            tabs.forEachIndexed { tabIndex, tab ->
                val selected = selectedTabIndex == tabIndex
                Tab(
                    selectedContentColor = Color.Black,
                    selected = selected,
                    onClick = { onTabClick(tabIndex) },
                    text = {
                        Text(
                            text = tab,
                            color = Color.White,
                            style = secondarySemiBoldHeadLineS
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(60))
                )
            }
        },
        modifier = Modifier
            .height(60.dp)
            .clip(RoundedCornerShape(60)),
    )
}

@Preview
@Composable
fun ProfilePostTypeTabPreview() {
    CustomTabRow(
        listOf("Posts", "Spots"),
        1,
        {}
    )
}