package com.madteam.sunset.ui.screens.addreview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.domain.usecases.GetSpotAttributesUseCase
import com.madteam.sunset.ui.screens.addreview.state.AddReviewUIEvent
import com.madteam.sunset.ui.screens.addreview.state.AddReviewUIState
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
    private val authRepository: AuthRepository,
    private val getSpotAttributesUseCase: GetSpotAttributesUseCase
) : ViewModel() {

    private val _userInfo: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())

    private val _state: MutableStateFlow<AddReviewUIState> = MutableStateFlow(AddReviewUIState())
    val state: StateFlow<AddReviewUIState> = _state

    init {
        getSpotAttributesList()
        getUserInfo()
    }

    fun onEvent(event: AddReviewUIEvent) {
        when (event) {
            is AddReviewUIEvent.ShowExitDialog -> {
                setShowExitDialog(event.state)
            }

            is AddReviewUIEvent.UpdateReviewTitle -> {
                modifyReviewTitle(event.text)
            }

            is AddReviewUIEvent.UpdateReviewDescription -> {
                modifyReviewDescription(event.text)
            }

            is AddReviewUIEvent.UpdateReviewScore -> {
                modifyReviewScore(event.score)
            }

            is AddReviewUIEvent.UpdateSelectedAttribute -> {
                modifySelectedAttributes(event.attribute)
            }

            is AddReviewUIEvent.CreateNewReview -> {
                createNewReview(event.spotReference)
            }

            is AddReviewUIEvent.ClearErrorToastText -> {
                clearErrorToastText()
            }

            is AddReviewUIEvent.ClearUploadProgress -> {
                clearUpdateProgressState()
            }

            is AddReviewUIEvent.ShowFinishedDialog -> {
                setShowFinishedDialog(event.state)
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                _userInfo.value = databaseRepository.getUserByEmail(user.email!!)
            }
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

    private fun modifySelectedAttributes(attribute: SpotAttribute) {
        if (!_state.value.selectedAttributes.contains(attribute)) {
            _state.value =
                _state.value.copy(selectedAttributes = _state.value.selectedAttributes + attribute)
        } else {
            _state.value =
                _state.value.copy(selectedAttributes = _state.value.selectedAttributes - attribute)
        }
    }

    private fun modifyReviewDescription(description: String) {
        _state.value = _state.value.copy(reviewDescription = description)
    }

    private fun modifyReviewTitle(title: String) {
        _state.value = _state.value.copy(reviewTitle = title)
    }

    private fun modifyReviewScore(score: Float) {
        _state.value = _state.value.copy(reviewScore = score.roundToInt())
    }

    private fun setShowExitDialog(state: Boolean) {
        _state.value = _state.value.copy(showExitDialog = state)
    }

    private fun setShowFinishedDialog(state: Boolean) {
        _state.value = _state.value.copy(showFinishedDialog = state)
    }

    private fun createNewReview(spotRef: String) {
        viewModelScope.launch {
            databaseRepository.createSpotReview(
                spotRef,
                title = _state.value.reviewTitle,
                description = _state.value.reviewDescription,
                attributeList = _state.value.selectedAttributes,
                score = _state.value.reviewScore,
                author = _userInfo.value
            ).collectLatest {
                _state.value = _state.value.copy(uploadProgress = it)
            }
        }
    }

    private fun clearUpdateProgressState() {
        _state.value = _state.value.copy(uploadProgress = Resource.Success(""))
    }

    private fun clearErrorToastText() {
        _state.value = _state.value.copy(errorToastText = -1)
    }

}