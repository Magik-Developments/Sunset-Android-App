package com.madteam.sunset.ui.screens.selectLocation

import androidx.lifecycle.ViewModel
import com.madteam.sunset.utils.googlemaps.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(

) : ViewModel() {

    private val _mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

}