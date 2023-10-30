package com.madteam.sunset.ui.screens.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.post.state.PostUIEvent
import com.madteam.sunset.ui.screens.post.state.PostUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var username: String = ""

    private val _state: MutableStateFlow<PostUIState> = MutableStateFlow(PostUIState())
    val state: StateFlow<PostUIState> = _state

    init {
        viewModelScope.launch {
            getUserUsername()
        }
    }

    fun onEvent(event: PostUIEvent) {
        when (event) {
            is PostUIEvent.ModifyUserPostLike -> {
                modifyUserPostLike()
            }

            is PostUIEvent.SetPostReference -> {
                setPostReference(event.postReference)
            }
        }
    }

    private fun getUserUsername() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                username = databaseRepository.getUserByEmail(user.email!!).username
            }
        }
    }

    private fun setPostReference(docReference: String) {
        _state.value = _state.value.copy(postReference = docReference)
        getPostInfo()
    }

    private fun getPostInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotPostByDocRef(_state.value.postReference)
                .collectLatest { post ->
                    _state.value = _state.value.copy(
                        postInfo = post,
                        postIsLiked = post.likedBy.contains(username.lowercase()),
                        postLikes = post.likes
                    )
                }
        }
    }

    private fun modifyUserPostLike() {
        viewModelScope.launch {
            databaseRepository.modifyUserPostLike(_state.value.postReference, username)
                .collectLatest {}
            if (_state.value.postIsLiked) {
                _state.value = _state.value.copy(postLikes = _state.value.postLikes - 1)
                _state.value = _state.value.copy(postIsLiked = false)
            } else {
                _state.value = _state.value.copy(postLikes = _state.value.postLikes + 1)
                _state.value = _state.value.copy(postIsLiked = true)
            }
        }
    }
}
