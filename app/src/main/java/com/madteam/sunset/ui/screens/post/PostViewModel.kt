package com.madteam.sunset.ui.screens.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _postInfo: MutableStateFlow<SpotPost> = MutableStateFlow(SpotPost())
    val postInfo: StateFlow<SpotPost> = _postInfo

    private val _postReference: MutableStateFlow<String> = MutableStateFlow("")

    fun setPostReference(docReference: String) {
        _postReference.value = docReference
        getPostInfo()
    }

    private fun getPostInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotPostByDocRef(_postReference.value).collect { post ->
                _postInfo.value = post
            }
        }
    }
}