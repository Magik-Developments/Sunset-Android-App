package com.madteam.sunset.ui.screens.spotdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.Spot
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _spotInfo: MutableStateFlow<Spot> = MutableStateFlow(Spot())
    val spotInfo: StateFlow<Spot> = _spotInfo

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _spotIsLiked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val spotIsLiked: StateFlow<Boolean> = _spotIsLiked

    private val _spotLikes: MutableStateFlow<Int> = MutableStateFlow(0)
    val spotLikes: StateFlow<Int> = _spotLikes

    private lateinit var username: String

    init {
        viewModelScope.launch {
            getUserUsername()
        }
    }

    private fun getUserUsername() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                databaseRepository.getUserByEmail(user.email!!) {
                    username = it.username
                }
            }
        }
    }

    fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    private fun getSpotInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collect { spot ->
                _spotInfo.value = spot
                _spotIsLiked.value = spot.likedBy.contains(username)
                _spotLikes.value = spot.likes
            }
        }
    }

    fun modifyUserSpotLike() {
        viewModelScope.launch {
            databaseRepository.modifyUserSpotLike(_spotReference.value, username)
                .collectLatest {}
            if (_spotIsLiked.value) {
                _spotLikes.value--
                _spotIsLiked.value = false
            } else {
                _spotLikes.value++
                _spotIsLiked.value = true
            }
        }
    }
}