package com.madteam.sunset.ui.screens.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.FilterScoreButton
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun BottomSheetFilterSpotsScreen(
    viewModel: FilterSpotsViewModel = hiltViewModel()
) {

    val filterScoreList by viewModel.filterScoreList.collectAsStateWithLifecycle()
    val selectedFilterScore by viewModel.selectedFilterScore.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BottomSheetFilterSpotsContent(
            filterScoreList = filterScoreList,
            selectedFilterScore = selectedFilterScore,
            onSelectedScoreFilterClicked = viewModel::updateSelectedFilterScore
        )
    }
}

@Composable
fun BottomSheetFilterSpotsContent(
    filterScoreList: List<Int>,
    selectedFilterScore: Int,
    onSelectedScoreFilterClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 8.dp)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CloseIconButton { /* to do */ }
            CustomSpacer(size = 16.dp)
            Text(text = "Filter Spots", style = secondarySemiBoldHeadLineS)
        }
        CustomSpacer(size = 24.dp)
        Text(text = "Score", style = secondarySemiBoldBodyM)
        CustomSpacer(size = 16.dp)
        FilterScoreButton(
            filterOptions = filterScoreList,
            selectedOption = selectedFilterScore,
            onOptionClicked = { onSelectedScoreFilterClicked(it) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheetEditProfileContent() {
    BottomSheetFilterSpotsContent(
        filterScoreList = listOf(4, 6, 8),
        selectedFilterScore = 4,
        onSelectedScoreFilterClicked = {}
    )
}