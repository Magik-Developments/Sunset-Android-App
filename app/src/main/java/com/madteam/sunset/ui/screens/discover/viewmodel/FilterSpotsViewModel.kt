package com.madteam.sunset.ui.screens.discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.domain.usecases.GetSpotAttributesUseCase
import com.madteam.sunset.ui.screens.discover.state.FilterSpotsUIEvent
import com.madteam.sunset.ui.screens.discover.state.FilterSpotsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSpotsViewModel @Inject constructor(
    private val getSpotAttributesUseCase: GetSpotAttributesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FilterSpotsUIState> =
        MutableStateFlow(FilterSpotsUIState())
    val state: StateFlow<FilterSpotsUIState> = _state

    init {
        getSpotAttributesList()
    }

    fun onEvent(event: FilterSpotsUIEvent) {
        when (event) {
            FilterSpotsUIEvent.ClearFilters -> {
                clearFilters()
            }

            is FilterSpotsUIEvent.UpdateSelectedFilterScore -> {
                updateSelectedFilterScore(event.score)
            }

            is FilterSpotsUIEvent.UpdateSelectedLocationFilter -> {
                updateSelectedLocationAttributes(event.attribute)
            }
        }
    }

    private fun updateSelectedFilterScore(score: Int) {
        if (_state.value.selectedFilterScore == score) {
            _state.value = _state.value.copy(selectedFilterScore = 0)
        } else {
            _state.value = _state.value.copy(selectedFilterScore = score)
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            val result = getSpotAttributesUseCase.invoke()
            if (result.isNotEmpty()) {
                _state.value = _state.value.copy(attributesList = result)
            }
        }
    }

    private fun updateSelectedLocationAttributes(attribute: SpotAttribute) {
        if (_state.value.selectedLocationFilter.contains(attribute)) {
            _state.value =
                _state.value.copy(selectedLocationFilter = _state.value.selectedLocationFilter - attribute)
        } else {
            _state.value =
                _state.value.copy(selectedLocationFilter = _state.value.selectedLocationFilter + attribute)
        }
    }

    private fun clearFilters() {
        _state.value = _state.value.copy(selectedFilterScore = 0)
        _state.value = _state.value.copy(selectedLocationFilter = emptyList())
    }

}