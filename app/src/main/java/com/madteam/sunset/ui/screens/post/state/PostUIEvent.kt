package com.madteam.sunset.ui.screens.post.state

sealed class PostUIEvent {
    data class SetPostReference(val postReference: String) : PostUIEvent()
    data object ModifyUserPostLike : PostUIEvent()
}