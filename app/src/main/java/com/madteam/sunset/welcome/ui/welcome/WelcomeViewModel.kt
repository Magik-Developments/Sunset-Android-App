@file:OptIn(ExperimentalMaterialApi::class)

package com.madteam.sunset.welcome.ui.welcome

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _sheetState: MutableStateFlow<ModalBottomSheetState> = MutableStateFlow(ModalBottomSheetState(Hidden))
    val sheetState: StateFlow<ModalBottomSheetState> = _sheetState

    fun expandBottomSheet() {
        viewModelScope.launch {
            _sheetState.value.animateTo(Expanded)
        }
    }
}
