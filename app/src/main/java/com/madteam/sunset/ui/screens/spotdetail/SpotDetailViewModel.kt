package com.madteam.sunset.ui.screens.spotdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.madteam.sunset.model.Spot
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _spotInfo: MutableStateFlow<Spot> = MutableStateFlow(Spot())
    val spotInfo: StateFlow<Spot> = _spotInfo

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    private fun getSpotInfo(){
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collect {spot ->
                _spotInfo.value = spot
            }
        }
    }
}