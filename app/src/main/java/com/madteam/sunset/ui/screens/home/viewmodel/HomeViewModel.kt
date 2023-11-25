package com.madteam.sunset.ui.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.data.repositories.SunsetRepository
import com.madteam.sunset.ui.screens.home.state.HomeUIEvent
import com.madteam.sunset.ui.screens.home.state.HomeUIState
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.calculateTimeDifference
import com.madteam.sunset.utils.convertHourToMilitaryFormat
import com.madteam.sunset.utils.getTimezone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sunsetRepository: SunsetRepository,
    private val locationRepository: LocationRepository,
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val state: StateFlow<HomeUIState> = _state

    init {
        getUserInfo()
        getLastSpots()
        getLastPosts()
    }

    fun onEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.LoadNextPostsPage -> {
                loadNextPostsPage()
            }

            is HomeUIEvent.LoadNextSpotsPage -> {
                loadNextSpotsPage()
            }

            is HomeUIEvent.ModifyUserPostLike -> {
                modifyUserPostLike(event.postId)
            }

            is HomeUIEvent.ModifyUserSpotLike -> {
                modifyUserSpotLike(event.spotId)
            }

            is HomeUIEvent.UpdateUserLocation -> {
                updateUserLocation(event.location)
            }

            is HomeUIEvent.ShowLocationPermissionDialog -> {
                updateLocationPermissionDialog(event.show)
            }
        }
    }

    private fun getLastPosts() {
        viewModelScope.launch {
            databaseRepository.getLastPosts(1, null).collectLatest {
                _state.value = _state.value.copy(postsList = it)
            }
        }
    }

    private fun getLastSpots() {
        viewModelScope.launch {
            databaseRepository.getLastSpots(
                itemsPerQuery = 1,
                lastItemId = null
            ).collectLatest {
                _state.value = _state.value.copy(spotsList = it)
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                _state.value =
                    _state.value.copy(userInfo = databaseRepository.getUserByEmail(user.email!!))
            }
        }
    }

    private fun loadNextSpotsPage() {
        viewModelScope.launch {
            databaseRepository.getLastSpots(
                itemsPerQuery = 1,
                lastItemId = _state.value.spotsList.last().id
            ).collectLatest { feed ->
                _state.value = _state.value.copy(spotsList = _state.value.spotsList + feed)
            }
        }
    }

    private fun loadNextPostsPage() {
        viewModelScope.launch {
            databaseRepository.getLastPosts(1, _state.value.postsList.last().id)
                .collectLatest { feed ->
                    _state.value = _state.value.copy(postsList = _state.value.postsList + feed)
                }
        }
    }

    private fun updateUserLocation(location: LatLng) {
        _state.value = _state.value.copy(userLocation = location)
        getUserLocality()
        getSunsetTimeBasedOnLocation()
        updateRemainingTimeToSunset()
    }

    private fun getUserLocality() {
        viewModelScope.launch {
            locationRepository.obtainLocalityFromLatLng(_state.value.userLocation).collectLatest {
                _state.value = _state.value.copy(userLocality = it)
            }
        }
    }

    private fun getSunsetTimeBasedOnLocation() {
        viewModelScope.launch {
            sunsetRepository.getSunsetTimeBasedOnLocation(
                latitude = _state.value.userLocation.latitude,
                longitude = _state.value.userLocation.longitude,
                timezone = getTimezone(),
                date = "today"
            ).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(sunsetTimeInformation = it.data!!)
                    }

                    else -> {
                        //Not necessary
                    }
                }

            }
        }
    }

    private fun updateRemainingTimeToSunset() {
        viewModelScope.launch {
            while (true) {
                _state.value = _state.value.copy(
                    remainingTimeToSunset = calculateTimeDifference(
                        convertHourToMilitaryFormat(
                            _state.value.sunsetTimeInformation.results.sunset
                        )
                    )
                )
                if (_state.value.sunsetTimeInformation != SunsetTimeResponse()) {
                    delay(60000)
                } else {
                    delay(1000)
                }
            }
        }
    }

    private fun modifyUserSpotLike(spotReference: String) {
        viewModelScope.launch {
            val indexToModify = _state.value.spotsList.indexOfFirst { it.id == spotReference }

            if (indexToModify != -1) {
                val spotLiked = _state.value.spotsList[indexToModify]

                databaseRepository.modifyUserSpotLike(
                    "spots/$spotReference",
                    _state.value.userInfo.username
                )
                    .collectLatest {}

                if (spotLiked.likedBy.contains(_state.value.userInfo.username)) {
                    val updatedList = _state.value.spotsList.toMutableList()
                    updatedList[indexToModify] =
                        spotLiked.copy(likedBy = spotLiked.likedBy - _state.value.userInfo.username)

                    _state.value = _state.value.copy(spotsList = updatedList)
                } else {
                    val updatedList = _state.value.spotsList.toMutableList()
                    updatedList[indexToModify] =
                        spotLiked.copy(likedBy = spotLiked.likedBy + _state.value.userInfo.username)
                    _state.value = _state.value.copy(spotsList = updatedList)
                }
            }
        }
    }

    private fun modifyUserPostLike(postReference: String) {
        viewModelScope.launch {
            val indexToModify = _state.value.postsList.indexOfFirst { it.id == postReference }

            if (indexToModify != -1) {
                val postLiked = _state.value.postsList[indexToModify]

                databaseRepository.modifyUserPostLike(
                    "posts/$postReference",
                    _state.value.userInfo.username
                )
                    .collectLatest {}

                if (postLiked.likedBy.contains(_state.value.userInfo.username)) {
                    val updatedList = _state.value.postsList.toMutableList()
                    updatedList[indexToModify] =
                        postLiked.copy(likedBy = postLiked.likedBy - _state.value.userInfo.username)

                    _state.value = _state.value.copy(postsList = updatedList)
                } else {
                    val updatedList = _state.value.postsList.toMutableList()
                    updatedList[indexToModify] =
                        postLiked.copy(likedBy = postLiked.likedBy + _state.value.userInfo.username)

                    _state.value = _state.value.copy(postsList = updatedList)
                }
            }
        }
    }

    private fun updateLocationPermissionDialog(show: Boolean) {
        _state.value = _state.value.copy(showLocationPermissionDialog = show)
    }

}