package com.madteam.sunset.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.domain.usecases.GetSpotAttributesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSpotsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val getSpotAttributesUseCase: GetSpotAttributesUseCase
) : ViewModel() {

    val filterScoreList = MutableStateFlow(listOf(4, 6, 8))

    private val _selectedFilterScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedFilterScore: StateFlow<Int> = _selectedFilterScore

    private val _attributesList: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val attributesList: StateFlow<List<SpotAttribute>> = _attributesList

    private val _selectedLocationFilter: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val selectedLocationFilter: StateFlow<List<SpotAttribute>> = _selectedLocationFilter

    init {
        getSpotAttributesList()
    }

    fun updateSelectedFilterScore(score: Int) {
        if (_selectedFilterScore.value == score) {
            _selectedFilterScore.value = 0
        } else {
            _selectedFilterScore.value = score
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            val result = getSpotAttributesUseCase.invoke()
            if (result.isNotEmpty()) {
                _attributesList.value = result
            }
        }
    }

    fun updateSelectedLocationAttributes(attribute: SpotAttribute) {
        if (_selectedLocationFilter.value.contains(attribute)) {
            _selectedLocationFilter.value = _selectedLocationFilter.value - attribute
        } else {
            _selectedLocationFilter.value = _selectedLocationFilter.value + attribute
        }
    }

    fun clearFilters() {
        _selectedLocationFilter.value = listOf()
        _selectedFilterScore.value = 0
    }

    fun applyFilters() {

    }

}