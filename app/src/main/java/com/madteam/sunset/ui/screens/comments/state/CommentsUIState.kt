package com.madteam.sunset.ui.screens.comments.state

import com.madteam.sunset.data.model.PostComment
import com.madteam.sunset.data.model.UserProfile

data class CommentsUIState(
    val comments: List<PostComment> = listOf(),
    val selectedComment: PostComment = PostComment(),
    val userInfo: UserProfile = UserProfile(),
    val isCommentAuthor: Boolean = false
)