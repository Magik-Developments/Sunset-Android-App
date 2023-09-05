package com.madteam.sunset.ui.screens.discover

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FilterSpotsViewModel @Inject constructor(

) : ViewModel() {

    val filterScoreList = MutableStateFlow(listOf(4, 6, 8))

    private val _selectedFilterScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedFilterScore: StateFlow<Int> = _selectedFilterScore

    fun updateSelectedFilterScore(score: Int) {
        if (_selectedFilterScore.value == score) {
            _selectedFilterScore.value = 0
        } else {
            _selectedFilterScore.value = score
        }
    }

}