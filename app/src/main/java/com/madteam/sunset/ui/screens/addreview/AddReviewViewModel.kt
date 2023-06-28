package com.madteam.sunset.ui.screens.addreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _attributesList: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val attributesList: StateFlow<List<SpotAttribute>> = _attributesList

    private val _selectedAttributes: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val selectedAttributes: StateFlow<List<SpotAttribute>> = _selectedAttributes

    private val _reviewTitle: MutableStateFlow<String> =
        MutableStateFlow("")
    val reviewTitle: StateFlow<String> = _reviewTitle

    private val _reviewDescription: MutableStateFlow<String> =
        MutableStateFlow("")
    val reviewDescription: StateFlow<String> = _reviewDescription

    private val _reviewScore: MutableStateFlow<Int> =
        MutableStateFlow(0)
    val reviewScore: StateFlow<Int> = _reviewScore

    private val _showExitDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog

    private val _userInfo: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())

    private val _uploadProgress: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Success(""))
    val uploadProgress: StateFlow<Resource<String>> = _uploadProgress

    private val _errorToastText: MutableStateFlow<String> = MutableStateFlow("")
    val errorToastText: StateFlow<String> = _errorToastText

    init {
        getSpotAttributesList()
        getUserInfo()
    }

    private fun getUserInfo() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                _userInfo.value = it
            }
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            databaseRepository.getAllSpotAttributes().collectLatest { attributesList ->
                _attributesList.value = attributesList
            }
        }
    }

    fun modifySelectedAttributes(attribute: SpotAttribute) {
        if (!_selectedAttributes.value.contains(attribute)) {
            _selectedAttributes.value = _selectedAttributes.value + attribute
        } else {
            _selectedAttributes.value = _selectedAttributes.value - attribute
        }
    }

    fun modifyReviewDescription(description: String) {
        _reviewDescription.value = description
    }

    fun modifyReviewTitle(title: String) {
        _reviewTitle.value = title
    }

    fun modifyReviewScore(score: Float) {
        _reviewScore.value = score.roundToInt()
    }

    fun setShowExitDialog(state: Boolean) {
        _showExitDialog.value = state
    }

    fun createNewReview(spotRef: String) {
        viewModelScope.launch {
            databaseRepository.createSpotReview(
                spotRef,
                _reviewTitle.value,
                _reviewDescription.value,
                _selectedAttributes.value,
                _reviewScore.value,
                _userInfo.value
            ).collectLatest {
                _uploadProgress.value = it
            }
        }
    }

    fun clearUpdateProgressState() {
        _uploadProgress.value = Resource.Success("")
    }

    fun clearErrorToastText() {
        _errorToastText.value = ""
    }

}