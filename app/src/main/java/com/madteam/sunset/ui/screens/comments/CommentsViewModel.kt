package com.madteam.sunset.ui.screens.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.PostComment
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
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

    private val _comments: MutableStateFlow<List<PostComment>> =
        MutableStateFlow(listOf())
    val comments: StateFlow<List<PostComment>> = _comments

    private val _postReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _selectedComment: MutableStateFlow<PostComment> =
        MutableStateFlow(PostComment())
    val selectedComment: StateFlow<PostComment> = _selectedComment

    private lateinit var username: String
    private lateinit var userImage: String

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                username = it.username
                userImage = it.image
            }
        }
    }

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

    fun addNewComment(commentText: String) {
        viewModelScope.launch {
            val newPostComment = PostComment(
                id = "",
                comment = commentText,
                author = UserProfile(username, "", "", "", "", "", userImage, false),
                creationDate = Calendar.getInstance().time.toString()
            )
            databaseRepository.createPostComment(newPostComment, _postReference.value)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            getCommentsInfo()
                        }

                        else -> {}
                    }
                }
        }
    }

    fun onSelectedComment(selectedComment: PostComment) {
        _selectedComment.value = selectedComment
    }

    fun unSelectComment() {
        _selectedComment.value = PostComment()
    }

    fun checkIfUserIsCommentAuthor(): Boolean {
        return _selectedComment.value.author.username.lowercase() == username.lowercase()
    }

    fun deleteSelectedComment() {
        viewModelScope.launch {
            databaseRepository.deletePostComment(_selectedComment.value, _postReference.value)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            unSelectComment()
                            getCommentsInfo()
                        }

                        else -> {}
                    }
                }
        }
    }
}