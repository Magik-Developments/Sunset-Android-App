package com.madteam.sunset.ui.screens.comments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.PostComment
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.comments.state.CommentsUIEvent
import com.madteam.sunset.ui.screens.comments.state.CommentsUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _postReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<CommentsUIState> = MutableStateFlow(CommentsUIState())
    val state: StateFlow<CommentsUIState> = _state

    init {
        getUserInfo()
    }

    fun onEvent(event: CommentsUIEvent) {
        when (event) {
            is CommentsUIEvent.OnCommentClick -> {
                onSelectedComment(event.postComment)
                checkIfUserIsCommentAuthor()
            }

            is CommentsUIEvent.OnAddComment -> {
                addNewComment(event.commentText)
            }

            CommentsUIEvent.OnCommentDeleted -> {
                deleteSelectedComment()
            }

            CommentsUIEvent.OnCommentUnselected -> {
                unSelectComment()
            }

            is CommentsUIEvent.SetPostReference -> {
                setPostReference(event.postReference)
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                _state.value = _state.value.copy(
                    userInfo = databaseRepository.getUserByEmail(user.email!!)
                )
            }
        }
    }

    private fun setPostReference(docReference: String) {
        _postReference.value = docReference
        getCommentsInfo()
    }

    private fun getCommentsInfo() {
        viewModelScope.launch {
            databaseRepository.getCommentsFromPostRef(_postReference.value)
                .collect { commentsList ->
                    _state.value = _state.value.copy(comments = commentsList)
                }
        }
    }

    private fun addNewComment(commentText: String) {
        viewModelScope.launch {
            val newPostComment = PostComment(
                id = "",
                comment = commentText,
                author = UserProfile(
                    username = _state.value.userInfo.username,
                    email = "",
                    provider = "",
                    creation_date = "",
                    name = "",
                    location = "",
                    image = _state.value.userInfo.image,
                    admin = false
                ),
                creationDate = Calendar.getInstance().time.toString()
            )
            databaseRepository.createPostComment(newPostComment, _postReference.value)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            getCommentsInfo()
                        }

                        else -> {
                            //Not implemented yet
                        }
                    }
                }
        }
    }

    private fun onSelectedComment(selectedComment: PostComment) {
        _state.value = _state.value.copy(
            selectedComment = selectedComment,
            isCommentAuthor = checkIfUserIsCommentAuthor()
        )
    }

    private fun unSelectComment() {
        _state.value = _state.value.copy(selectedComment = PostComment())
    }

    private fun checkIfUserIsCommentAuthor(): Boolean =
        _state.value.selectedComment.author.username.lowercase() == _state.value.userInfo.username.lowercase()

    private fun deleteSelectedComment() {
        viewModelScope.launch {
            databaseRepository.deletePostComment(_state.value.selectedComment, _postReference.value)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            unSelectComment()
                            getCommentsInfo()
                        }

                        else -> {
                            //Not implemented yet
                        }
                    }
                }
        }
    }
}