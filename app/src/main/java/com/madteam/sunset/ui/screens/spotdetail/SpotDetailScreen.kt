package com.madteam.sunset.ui.screens.spotdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.firestore.GeoPoint
import com.madteam.sunset.R
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.SpotReview
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.IconButtonDark
import com.madteam.sunset.ui.common.ImagePostCard
import com.madteam.sunset.ui.common.LargeLightButton
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightBackButton
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.primaryMediumDisplayS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.utils.getResourceId
import com.madteam.sunset.utils.openDirectionsOnGoogleMaps

@Composable
fun SpotDetailScreen(
    navController: NavController,
    viewModel: SpotDetailViewModel = hiltViewModel(),
    spotReference: String
) {

    viewModel.setSpotReference("spots/$spotReference")
    val spotInfo by viewModel.spotInfo.collectAsStateWithLifecycle()

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SpotDetailContent(
                    spotInfo = spotInfo
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun SpotDetailContent(
    spotInfo: Spot
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, backIconButton, saveIconButton, sendIconButton, likeIconButton) = createRefs()
            AutoSlidingCarousel(
                itemsCount = spotInfo.featuredImages.size,
                itemContent = { index ->
                    GlideImage(
                        model = spotInfo.featuredImages[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop
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
            RoundedLightBackButton(modifier = Modifier.constrainAs(backIconButton) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 24.dp)
            }, onClick = {})
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
        //Spotter user information
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotterImage, spottedByTitle, spottedByUsername, createdDate) = createRefs()
            ProfileImage(
                imageUrl = spotInfo.spottedBy.image,
                size = 56.dp,
                modifier = Modifier.constrainAs(spotterImage) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(parent.top, (-16).dp)
                })
            Text(
                text = "Spotted by",
                style = secondaryRegularBodyS,
                modifier = Modifier.constrainAs(spottedByTitle) {
                    top.linkTo(parent.top, 4.dp)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = "@${spotInfo.spottedBy.username}",
                style = primaryBoldHeadlineXS,
                modifier = Modifier.constrainAs(spottedByUsername) {
                    top.linkTo(spottedByTitle.bottom)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = "created ${spotInfo.creationDate}",
                style = secondaryRegularBodyS,
                color = Color(0xFF333333),
                modifier = Modifier.constrainAs(createdDate) {
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(parent.top, 4.dp)
                })
        }
        CustomSpacer(size = 24.dp)
        //Spot title and description
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotTitle, spotDescription) = createRefs()
            Text(
                text = spotInfo.name,
                style = primaryMediumDisplayS,
                modifier = Modifier
                    .constrainAs(spotTitle) {
                        start.linkTo(parent.start, 24.dp)
                    }
                    .padding(end = 24.dp))
            Text(
                text = spotInfo.description,
                style = secondaryRegularBodyM,
                modifier = Modifier
                    .constrainAs(spotDescription) {
                        start.linkTo(parent.start, 24.dp)
                        top.linkTo(spotTitle.bottom, 8.dp)
                    }
                    .padding(end = 36.dp)
            )
        }
        CustomSpacer(size = 16.dp)
        //Spot information and location
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Brightness5, contentDescription = "")
            CustomSpacer(size = 4.dp)
            Text(text = "${spotInfo.score}", style = secondaryRegularBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "·", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "Visited ", style = secondaryRegularBodyM)
            Text(text = "${spotInfo.visitedTimes} times", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "·", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "${spotInfo.likes} likes", style = secondarySemiBoldBodyM)
        }
        CustomSpacer(size = 8.dp)
        Text(
            text = spotInfo.location,
            style = secondaryRegularBodyL,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 16.dp)
        //Take me there section
        Divider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color(0xFF999999)
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            val (distanceText, distance, time, directions) = createRefs()
            Text(
                text = "You are only ",
                style = secondaryRegularBodyL,
                modifier = Modifier.constrainAs(distanceText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, 8.dp)
                })
            Text(
                text = "2 km away",
                style = secondarySemiBoldBodyL,
                modifier = Modifier.constrainAs(distance) {
                    start.linkTo(distanceText.end)
                    top.linkTo(parent.top, 8.dp)
                })
            Text(
                text = "or 10 minutes walking",
                style = secondaryRegularBodyM,
                modifier = Modifier.constrainAs(time) {
                    start.linkTo(distanceText.start)
                    end.linkTo(distance.end)
                    top.linkTo(distance.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                })
            Icon(
                modifier = Modifier
                    .constrainAs(directions) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, 36.dp)
                    }
                    .size(48.dp)
                    .clickable {
                        openDirectionsOnGoogleMaps(context, spotInfo.locationInLatLng)
                    },
                imageVector = Icons.Filled.Directions,
                contentDescription = "",
                tint = Color(0xFFFFB600)
            )
        }
        Divider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color(0xFF999999)
        )
        //About this spot section
        CustomSpacer(size = 16.dp)
        Text(
            text = "About this spot",
            style = secondarySemiBoldHeadLineM,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 16.dp)
        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(spotInfo.attributes) { _, attribute ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = getResourceId(
                                    attribute.icon,
                                    context
                                )
                            ),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = attribute.title,
                            style = secondaryRegularBodyS,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }
        CustomSpacer(size = 24.dp)
        Divider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color(0xFF999999)
        )
        CustomSpacer(size = 24.dp)
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Brightness7, contentDescription = null)
            CustomSpacer(size = 8.dp)
            Text(text = spotInfo.score.toString(), style = secondarySemiBoldHeadLineM)
            Text(text = " · ", style = secondarySemiBoldHeadLineM)
            Text(text = "${spotInfo.spotReviews.size} reviews", style = secondarySemiBoldHeadLineM)
        }
        CustomSpacer(size = 16.dp)
        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(spotInfo.spotReviews) { _, review ->
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        val (title, description, attributesTitle, userImage, userUsername, reviewDate, scoreIcon, score) = createRefs()
                        Text(
                            text = review.title,
                            style = secondarySemiBoldBodyL,
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(parent.top, 16.dp)
                                start.linkTo(parent.start)
                            })
                        Text(
                            text = review.description,
                            style = secondaryRegularBodyM,
                            modifier = Modifier.constrainAs(description) {
                                top.linkTo(title.bottom, 8.dp)
                                start.linkTo(parent.start)
                            })
                        ProfileImage(
                            imageUrl = review.postedBy.image,
                            size = 40.dp,
                            modifier = Modifier.constrainAs(userImage) {
                                bottom.linkTo(parent.bottom, 16.dp)
                            })
                        Text(
                            text = "@${review.postedBy.username}",
                            style = secondarySemiBoldBodyM,
                            modifier = Modifier.constrainAs(userUsername) {
                                top.linkTo(userImage.top, 4.dp)
                                start.linkTo(userImage.end, 4.dp)
                            })
                        Text(
                            text = review.creationDate,
                            style = secondaryRegularBodyM,
                            modifier = Modifier.constrainAs(reviewDate) {
                                bottom.linkTo(userImage.bottom, 4.dp)
                                start.linkTo(userImage.end, 4.dp)
                            })
                        Text(
                            text = review.score.toString(),
                            style = secondarySemiBoldHeadLineM,
                            modifier = Modifier.constrainAs(score) {
                                end.linkTo(parent.end)
                                bottom.linkTo(userImage.bottom)
                                top.linkTo(userImage.top)
                            })
                        Icon(
                            imageVector = Icons.Filled.Brightness7,
                            contentDescription = null,
                            modifier = Modifier.constrainAs(scoreIcon) {
                                end.linkTo(score.start, 4.dp)
                                top.linkTo(score.top)
                                bottom.linkTo(score.bottom)
                            })
                        Box(modifier = Modifier.constrainAs(attributesTitle) {
                            top.linkTo(description.bottom)
                            bottom.linkTo(userImage.top)
                        }) {
                            Column() {
                                Text(
                                    text = "How it was?",
                                    style = secondarySemiBoldBodyM
                                )
                                LazyRow(
                                    modifier = Modifier.padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    itemsIndexed(review.spotAttributes) { _, attribute ->
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .border(
                                                    1.dp,
                                                    Color(0xFF999999),
                                                    RoundedCornerShape(8.dp)
                                                )
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Icon(
                                                    painter = painterResource(
                                                        id = getResourceId(
                                                            attribute.icon,
                                                            context
                                                        )
                                                    ),
                                                    contentDescription = "",
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        CustomSpacer(size = 16.dp)
        Row(
            Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            LargeLightButton(
                onClick = { /*TODO*/ },
                text = R.string.show_all_reviews,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            )
            IconButtonDark(
                buttonIcon = Icons.Filled.Add,
                description = R.string.add,
                onClick = {},
                iconTint = Color.White
            )
        }
        CustomSpacer(size = 24.dp)
        Divider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color(0xFF999999)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = "User posts",
            style = secondarySemiBoldHeadLineM,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 16.dp)
        LazyRow(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(spotInfo.spotPosts) { _, post ->
                ImagePostCard(cardSize = 250.dp, postInfo = post)
            }
        }
        CustomSpacer(size = 16.dp)
        Row(
            Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            LargeLightButton(
                onClick = { /*TODO*/ },
                text = R.string.show_all_posts,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            )
            IconButtonDark(
                buttonIcon = Icons.Filled.Add,
                description = R.string.add,
                onClick = {},
                iconTint = Color.White
            )
        }
    }
}

@Preview(heightDp = 2000, showBackground = true)
@Composable
fun SpotDetailContentPreview() {
    SpotDetailContent(
        Spot(
            id = "test",
            spottedBy = UserProfile(
                "SunsetApp",
                "",
                "",
                "",
                "",
                "",
                ""
            ),
            featuredImages = listOf(),
            creationDate = "15/11/2023",
            name = "La piscina de Vallparadís",
            description = LoremIpsum(300).toString(),
            score = 9.3f,
            visitedTimes = 4524,
            likes = 1511,
            locationInLatLng = GeoPoint(0.0, 0.0),
            location = "Terrassa, Miami, Cataluña",
            attributes = listOf(
                SpotAttribute(
                    "",
                    "",
                    "There is parking",
                    "ic_local_parking",
                    true
                ),
                SpotAttribute(
                    "",
                    "",
                    "There is parking",
                    "ic_local_parking",
                    true
                )
            ),
            spotReviews = listOf(
                SpotReview(
                    "",
                    "El mejor atardecer de mi vida, volveré 100%!! 😎",
                    "Experiencia inolvidable ☀️🌅",
                    postedBy = UserProfile("adriafa", "addd", "", "", "", "", ""),
                    spotAttributes = listOf(
                        SpotAttribute(
                            "",
                            "Site to park here",
                            "d",
                            "ic_local_parking",
                            true
                        ),
                        SpotAttribute("", "Site to park here", "d", "ic_groups", true)
                    ),
                    creationDate = "23/05/2023",
                    score = 9.8f
                )
            ),
            spotPosts = listOf(
                SpotPost(
                    "", "", "", listOf(
                        "https://cdn.vox-cdn.com/thumbor/Al48-pEnyIn2rlgKX7MIHNmlE68=/0x0:5563x3709/1200x800/filters:focal(2302x1311:3192x2201)/cdn.vox-cdn.com/uploads/chorus_image/image/65752607/1048232144.jpg.0.jpg",
                        "https://images.pexels.com/photos/3651752/pexels-photo-3651752.jpeg?cs=srgb&dl=pexels-anas-hinde-3651752.jpg&fm=jpg"
                    ),
                    UserProfile("dgalaaa", "", "", "", "", "Barcelona, ES.", ""),
                    ""
                )
            )
        )
    )
}
