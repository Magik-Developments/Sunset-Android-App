package com.madteam.sunset.ui.screens.addreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _attributesList: MutableStateFlow<List<SpotAttribute>> = MutableStateFlow(
        mutableListOf()
    )
    val attributesList: StateFlow<List<SpotAttribute>> = _attributesList

    init {
        getSpotAttributesList()
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            databaseRepository.getAllSpotAttributes().collectLatest { attributesList ->
                _attributesList.value = attributesList
            }
        }
    }
}