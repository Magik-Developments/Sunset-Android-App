package com.madteam.sunset.ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.utils.shimmerBrush

@Composable
fun PostScreen(

) {
    PostContent(
        SpotPost()
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun PostContent(
    postInfo: SpotPost
) {
    val scrollState = rememberScrollState()
    val showShimmer = remember { mutableStateOf(true) }
    val context = LocalContext.current

    if (postInfo != SpotPost()) {
        showShimmer.value = false
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, saveIconButton, sendIconButton, likeIconButton) = createRefs()
            AutoSlidingCarousel(
                itemsCount = postInfo.images.size,
                itemContent = { index ->
                    GlideImage(
                        model = postInfo.images[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
                    .constrainAs(spotImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

            )
            RoundedLightSaveButton(onClick = {}, modifier = Modifier.constrainAs(saveIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
            }, isSaved = false)
            RoundedLightSendButton(modifier = Modifier.constrainAs(sendIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(saveIconButton.start, 16.dp)
            }, onClick = {})
            RoundedLightLikeButton(onClick = {}, modifier = Modifier.constrainAs(likeIconButton) {
                end.linkTo(parent.end, 24.dp)
                bottom.linkTo(parent.bottom, 16.dp)
            }, isLiked = false)
        }

        //Post author user information
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotterImage, postedByTitle, postedByUsername, createdDate) = createRefs()
            ProfileImage(
                imageUrl = postInfo.author.image,
                size = 56.dp,
                modifier = Modifier.constrainAs(spotterImage) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(parent.top, (-16).dp)
                })
            Text(
                text = "Posted by",
                style = secondaryRegularBodyS,
                modifier = Modifier.constrainAs(postedByTitle) {
                    top.linkTo(parent.top, 4.dp)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    "@${postInfo.author.username}"
                },
                style = primaryBoldHeadlineXS,
                modifier = Modifier
                    .constrainAs(postedByUsername) {
                        top.linkTo(postedByTitle.bottom)
                        start.linkTo(spotterImage.end, 8.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 100.dp))
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    postInfo.creation_date
                },
                style = secondaryRegularBodyS,
                color = Color(0xFF333333),
                modifier = Modifier
                    .constrainAs(createdDate) {
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(parent.top, 4.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 100.dp)
            )
        }
        CustomSpacer(size = 24.dp)
        //Post description
        Text(
            text = postInfo.description,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis,
            style = secondaryRegularBodyM,
            modifier = Modifier

                .background(shimmerBrush(showShimmer = showShimmer.value))
                .defaultMinSize(minWidth = 400.dp)
        )
        CustomSpacer(size = 4.dp)
        Text(text = "${postInfo.likes} likes", style = secondarySemiBoldBodyM)
        CustomSpacer(size = 4.dp)
        if (postInfo.comments.isNotEmpty()) {
            Text(text = "View all ${postInfo.comments.size} comments")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PostContentPreview() {
    PostContent(SpotPost())
}