package com.madteam.sunset.ui.screens.discover.state

import com.madteam.sunset.data.model.SpotAttribute

data class FilterSpotsUIState(
    val selectedFilterScore: Int = 0,
    val selectedLocationFilter: List<SpotAttribute> = emptyList(),
    val attributesList: List<SpotAttribute> = emptyList(),
    val filterScoreList: List<Int> = listOf(4, 6, 8)
)