package com.madteam.sunset.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.madteam.sunset.R.string
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.formatDate
import com.madteam.sunset.utils.shimmerBrush
import kotlinx.coroutines.delay

private const val AUTO_SLIDE_DURATION = 5000

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImage(
    imageUrl: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val showShimmer = remember { mutableStateOf(true) }
    if (imageUrl.isNotBlank()) {
        showShimmer.value = false
    }

    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(string.profile_user_image_description),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .background(shimmerBrush(showShimmer = showShimmer.value), shape = CircleShape)
            .clip(CircleShape)
    )
}

@Composable
fun ImageSliderCounter(
    actualImage: Int,
    totalImages: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0x80FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .height(18.dp)
            .width(40.dp), contentAlignment = Alignment.Center
    ) {
        Row {
            Text(text = actualImage.toString(), style = secondarySemiBoldBodyS, color = Color.White)
            Text(text = " / ", style = secondarySemiBoldBodyS, color = Color.White)
            Text(text = totalImages.toString(), style = secondarySemiBoldBodyS, color = Color.White)
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Int = AUTO_SLIDE_DURATION,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    if (autoSlideDuration != 0) {
        LaunchedEffect(pagerState.currentPage) {
            delay(autoSlideDuration.toLong())
            pagerState.animateScrollToPage(
                (pagerState.currentPage + 1) % if (itemsCount == 0) {
                    1
                } else {
                    itemsCount
                }
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = 4000f
                    )
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            if (itemsCount != 0) {
                ImageSliderCounter(
                    actualImage = (if (isDragged) pagerState.currentPage else pagerState.targetPage) + 1,
                    totalImages = itemsCount,
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImagePostCard(
    cardSize: Dp,
    postInfo: SpotPost,
    onItemClicked: () -> Unit
) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(20.dp)) {
        ConstraintLayout(
            modifier = Modifier
                .size(cardSize)
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .clickable {
                    onItemClicked()
                }
        ) {
            val (userBackground, userImage, userUsername, userLocation, image) = createRefs()
            GlideImage(
                model = postInfo.images.first(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(50.dp))
                    .constrainAs(userBackground) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                    })
            Text(
                text = "@${postInfo.author.username}",
                style = secondarySemiBoldBodyS,
                modifier = Modifier.constrainAs(userUsername) {
                    start.linkTo(userImage.end, 8.dp)
                    top.linkTo(userBackground.top, 8.dp)
                })
            ProfileImage(
                imageUrl = postInfo.author.image,
                size = 35.dp,
                modifier = Modifier.constrainAs(userImage) {
                    start.linkTo(userBackground.start, 4.dp)
                    top.linkTo(userBackground.top)
                    bottom.linkTo(userBackground.bottom)
                })
            Text(
                text = formatDate(postInfo.creation_date),
                style = secondaryRegularBodyS,
                modifier = Modifier.constrainAs(userLocation) {
                    start.linkTo(userUsername.start)
                    top.linkTo(userUsername.bottom, (-2).dp)
                })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImagePostCardProfile(
    postInfo: SpotPost,
    onItemClicked: () -> Unit
) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(20.dp), modifier =
    Modifier
        .size(180.dp)
        .clip(
            RoundedCornerShape(20.dp)
        )
        .clickable { onItemClicked() }) {
        GlideImage(
            model = postInfo.images.first(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageSpotCardProfile(
    spotInfo: Spot,
    onItemClicked: () -> Unit
) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(20.dp)) {
        ConstraintLayout(
            modifier = Modifier
                .size(180.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .clickable {
                    onItemClicked()
                }
        ) {
            val (spotName, image) = createRefs()
            GlideImage(
                model = spotInfo.featuredImages.first(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = 4000f
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {}
            Text(
                text = spotInfo.name,
                style = secondarySemiBoldHeadLineS,
                color = Color.White,
                modifier = Modifier.constrainAs(spotName) {
                    start.linkTo(parent.start, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                }
            )
        }

    }
}