package com.madteam.sunset.ui.screens.discover.state

import com.madteam.sunset.data.model.SpotAttribute

sealed class FilterSpotsUIEvent {
    data class UpdateSelectedFilterScore(val score: Int) : FilterSpotsUIEvent()
    data class UpdateSelectedLocationFilter(val attribute: SpotAttribute) : FilterSpotsUIEvent()
    data object ClearFilters : FilterSpotsUIEvent()

}