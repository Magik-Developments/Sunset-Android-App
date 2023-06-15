package com.madteam.sunset.ui.screens.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.PostComment
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _comments: MutableStateFlow<List<PostComment>> =
        MutableStateFlow(listOf<PostComment>())
    val comments: StateFlow<List<PostComment>> = _comments

    private val _postReference: MutableStateFlow<String> = MutableStateFlow("")

    fun setPostReference(docReference: String) {
        _postReference.value = docReference
        getCommentsInfo()
    }

    private fun getCommentsInfo() {
        viewModelScope.launch {
            databaseRepository.getCommentsFromPostRef(_postReference.value)
                .collect { commentsList ->
                    _comments.value = commentsList
                }
        }
    }
}