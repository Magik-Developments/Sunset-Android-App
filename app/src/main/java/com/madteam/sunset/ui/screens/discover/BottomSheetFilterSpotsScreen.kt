package com.madteam.sunset.ui.screens.discover

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.R
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.FilterAttributesButton
import com.madteam.sunset.ui.common.FilterScoreButton
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SmallButtonSunset
import com.madteam.sunset.ui.screens.addreview.LOCATION_ATTRIBUTES
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun BottomSheetFilterSpotsScreen(
    viewModel: FilterSpotsViewModel = hiltViewModel(),
    onCloseClicked: () -> Unit,
    applyFilters: (Int, List<SpotAttribute>) -> Unit
) {

    val filterScoreList by viewModel.filterScoreList.collectAsStateWithLifecycle()
    val selectedFilterScore by viewModel.selectedFilterScore.collectAsStateWithLifecycle()
    val attributesList by viewModel.attributesList.collectAsStateWithLifecycle()
    val selectedLocationAttributes by viewModel.selectedLocationFilter.collectAsStateWithLifecycle()

    BackHandler {
        onCloseClicked()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BottomSheetFilterSpotsContent(
            attributesList = attributesList,
            filterScoreList = filterScoreList,
            selectedFilterScore = selectedFilterScore,
            onSelectedScoreFilterClicked = viewModel::updateSelectedFilterScore,
            onCloseClicked = onCloseClicked,
            onSelectedLocationFilterClicked = viewModel::updateSelectedLocationAttributes,
            selectedFilterLocation = selectedLocationAttributes,
            onClearClicked = viewModel::clearFilters,
            onFilterApplied = {
                applyFilters(
                    selectedFilterScore,
                    selectedLocationAttributes
                )
                onCloseClicked()
            }
        )
    }
}

@Composable
fun BottomSheetFilterSpotsContent(
    attributesList: List<SpotAttribute>,
    onSelectedLocationFilterClicked: (SpotAttribute) -> Unit,
    selectedFilterLocation: List<SpotAttribute>,
    filterScoreList: List<Int>,
    selectedFilterScore: Int,
    onSelectedScoreFilterClicked: (Int) -> Unit,
    onCloseClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onFilterApplied: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 16.dp)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CloseIconButton { onCloseClicked() }
            CustomSpacer(size = 16.dp)
            Text(text = "Filter Spots", style = secondarySemiBoldHeadLineS)
        }
        CustomSpacer(size = 24.dp)
        Text(text = stringResource(id = R.string.score), style = secondarySemiBoldBodyM)
        CustomSpacer(size = 16.dp)
        FilterScoreButton(
            filterOptions = filterScoreList,
            selectedOption = selectedFilterScore,
            onOptionClicked = { onSelectedScoreFilterClicked(it) }
        )
        CustomSpacer(size = 24.dp)
        Text(text = stringResource(id = R.string.environment), style = secondarySemiBoldBodyM)
        CustomSpacer(size = 16.dp)
        FilterAttributesButton(
            filterOptions = attributesList.filter { it.type == LOCATION_ATTRIBUTES },
            selectedOptions = selectedFilterLocation,
            onOptionClicked = { onSelectedLocationFilterClicked(it) }
        )
        CustomSpacer(size = 24.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SmallButtonDark(
                onClick = { onClearClicked() },
                text = R.string.clear,
                enabled = selectedFilterScore != 0 || selectedFilterLocation.isNotEmpty()
            )
            SmallButtonSunset(
                onClick = { onFilterApplied() },
                text = R.string.apply,
                enabled = true
            )
        }
        CustomSpacer(size = 16.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheetEditProfileContent() {
    /* BottomSheetFilterSpotsContent(
         filterScoreList = listOf(4, 6, 8),
         selectedFilterScore = 4,
         onSelectedScoreFilterClicked = {},
         onCloseClicked = {}
     ) */
}