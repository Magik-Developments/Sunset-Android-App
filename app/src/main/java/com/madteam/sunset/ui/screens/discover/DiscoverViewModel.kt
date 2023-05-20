package com.madteam.sunset.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.utils.googlemaps.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val _selectedCluster: MutableStateFlow<SpotClusterItem?> = MutableStateFlow(null)
    val selectedCluster: StateFlow<SpotClusterItem?> = _selectedCluster

    private val _mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

    init {
        viewModelScope.launch {
            databaseRepository.getSpotsLocations().collect { spots ->
                _mapState.value = _mapState.value.copy(clusterItems = spots)
            }
        }
    }

    fun toggleClusterVisibility(clusterItem: SpotClusterItem?) {
        _selectedCluster.value = clusterItem?.copy(
            isVisible = !clusterItem.isVisible
        )
    }
}