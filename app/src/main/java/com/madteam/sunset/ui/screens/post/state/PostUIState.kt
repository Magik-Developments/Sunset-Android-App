package com.madteam.sunset.ui.screens.post.state

import com.madteam.sunset.data.model.SpotPost

data class PostUIState(
    val postInfo: SpotPost = SpotPost(),
    val postIsLiked: Boolean = false,
    val postLikes: Int = 0,
    val postReference: String = "",
)
