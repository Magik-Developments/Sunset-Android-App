package com.madteam.sunset.ui.screens.comments.state

import com.madteam.sunset.data.model.PostComment

sealed class CommentsUIEvent {
    data class SetPostReference(val postReference: String) : CommentsUIEvent()
    data class OnCommentClick(val postComment: PostComment) : CommentsUIEvent()
    data class OnAddComment(val commentText: String) : CommentsUIEvent()
    data object OnCommentUnselected : CommentsUIEvent()
    data object OnCommentDeleted : CommentsUIEvent()

}
