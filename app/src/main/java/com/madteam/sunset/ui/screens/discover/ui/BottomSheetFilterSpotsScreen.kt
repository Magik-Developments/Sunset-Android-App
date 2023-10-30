package com.madteam.sunset.ui.screens.discover.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.FilterAttributesButton
import com.madteam.sunset.ui.common.FilterScoreButton
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SmallButtonSunset
import com.madteam.sunset.ui.screens.addreview.ui.SUNSET_ATTRIBUTES
import com.madteam.sunset.ui.screens.discover.state.FilterSpotsUIEvent
import com.madteam.sunset.ui.screens.discover.state.FilterSpotsUIState
import com.madteam.sunset.ui.screens.discover.viewmodel.FilterSpotsViewModel
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun BottomSheetFilterSpotsScreen(
    viewModel: FilterSpotsViewModel = hiltViewModel(),
    onCloseClicked: () -> Unit,
    applyFilters: (
        selectedFilterScore: Int,
        selectedLocationAttributes: List<SpotAttribute>
    ) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
            state = state,
            onSelectedScoreFilterClicked = {
                viewModel.onEvent(
                    FilterSpotsUIEvent.UpdateSelectedFilterScore(
                        it
                    )
                )
            },
            onCloseClicked = { onCloseClicked() },
            onSelectedLocationFilterClicked = {
                viewModel.onEvent(
                    FilterSpotsUIEvent.UpdateSelectedLocationFilter(
                        it
                    )
                )
            },
            onClearClicked = { viewModel.onEvent(FilterSpotsUIEvent.ClearFilters) },
            onFilterApplied = {
                applyFilters(
                    state.selectedFilterScore,
                    state.selectedLocationFilter
                )
                onCloseClicked()
            }
        )
    }
}

@Composable
fun BottomSheetFilterSpotsContent(
    state: FilterSpotsUIState,
    onSelectedLocationFilterClicked: (SpotAttribute) -> Unit,
    onSelectedScoreFilterClicked: (Int) -> Unit,
    onCloseClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onFilterApplied: () -> Unit
) {
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
            filterOptions = state.filterScoreList,
            selectedOption = state.selectedFilterScore,
            onOptionClicked = { onSelectedScoreFilterClicked(it) }
        )
        CustomSpacer(size = 24.dp)
        Text(text = stringResource(id = R.string.environment), style = secondarySemiBoldBodyM)
        CustomSpacer(size = 16.dp)
        FilterAttributesButton(
            filterOptions = state.attributesList.filter { it.type == SUNSET_ATTRIBUTES },
            selectedOptions = state.selectedLocationFilter,
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
                enabled = state.selectedFilterScore != 0 || state.selectedLocationFilter.isNotEmpty()
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