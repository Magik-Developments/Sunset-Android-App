package com.madteam.sunset.ui.screens.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
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

    private val _postInfo: MutableStateFlow<SpotPost> = MutableStateFlow(SpotPost())
    val postInfo: StateFlow<SpotPost> = _postInfo

    private val _postIsLiked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val postIsLiked: StateFlow<Boolean> = _postIsLiked

    private val _postLikes: MutableStateFlow<Int> = MutableStateFlow(0)
    val postLikes: StateFlow<Int> = _postLikes

    private val _postReference: MutableStateFlow<String> = MutableStateFlow("")

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

    fun setPostReference(docReference: String) {
        _postReference.value = docReference
        getPostInfo()
    }

    fun getPostInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotPostByDocRef(_postReference.value).collectLatest { post ->
                _postInfo.value = post
                _postIsLiked.value = post.likedBy.contains(username)
                _postLikes.value = post.likes
            }
        }
    }

    fun modifyUserPostLike() {
        viewModelScope.launch {
            databaseRepository.modifyUserPostLike(_postReference.value, username)
                .collectLatest {
                }
        }
    }
}
