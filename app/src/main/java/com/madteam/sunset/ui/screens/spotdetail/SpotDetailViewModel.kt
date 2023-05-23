package com.madteam.sunset.ui.screens.spotdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.Spot
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _spotInfo: MutableStateFlow<Spot> = MutableStateFlow(Spot())
    val spotInfo: StateFlow<Spot> = _spotInfo

    init {
        viewModelScope.launch {
            databaseRepository.
        }
    }
}