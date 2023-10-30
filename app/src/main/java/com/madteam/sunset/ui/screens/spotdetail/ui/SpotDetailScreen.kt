package com.madteam.sunset.ui.screens.spotdetail.ui

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalDistance
import com.madteam.sunset.R
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.ui.common.AttributeInfoDialog
import com.madteam.sunset.ui.common.AttributesBigListRow
import com.madteam.sunset.ui.common.AttributesSmallListRow
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.IconButtonDark
import com.madteam.sunset.ui.common.ImagePostCard
import com.madteam.sunset.ui.common.LargeLightButton
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.ReportDialog
import com.madteam.sunset.ui.common.RoundedLightBackButton
import com.madteam.sunset.ui.common.RoundedLightEditButton
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightReportButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.screens.spotdetail.state.SpotDetailUIEvent
import com.madteam.sunset.ui.screens.spotdetail.state.SpotDetailUIState
import com.madteam.sunset.ui.screens.spotdetail.viewmodel.SpotDetailViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.primaryMediumDisplayS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.utils.formatDate
import com.madteam.sunset.utils.generateDeepLink
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.getShareIntent
import com.madteam.sunset.utils.openDirectionsOnGoogleMaps
import com.madteam.sunset.utils.shimmerBrush
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpotDetailScreen(
    navController: NavController,
    viewModel: SpotDetailViewModel = hiltViewModel(),
    spotReference: String
) {

    viewModel.onEvent(SpotDetailUIEvent.SetSpotReference("spots/$spotReference"))
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        content = { _ ->
            Box(
                modifier = Modifier.padding(0.dp)
            ) {
                SpotDetailContent(
                    state = state,
                    navController = navController,
                    navigateTo = navController::navigate,
                    spotLikeClick = { viewModel.onEvent(SpotDetailUIEvent.ModifyUserSpotLike) },
                    updateUserLocation = { viewModel.onEvent(SpotDetailUIEvent.UpdateUserLocation(it)) },
                    setShowReportDialog = {
                        viewModel.onEvent(
                            SpotDetailUIEvent.SetShowReportDialog(
                                it
                            )
                        )
                    },
                    setSelectedReportOption = {
                        viewModel.onEvent(
                            SpotDetailUIEvent.SetSelectedReportOption(
                                it
                            )
                        )
                    },
                    setAdditionalReportInformation = {
                        viewModel.onEvent(
                            SpotDetailUIEvent.SetAdditionalReportInformation(
                                it
                            )
                        )
                    },
                    sendReportButton = { viewModel.onEvent(SpotDetailUIEvent.SendReport) },
                    setReportSentDialog = { viewModel.onEvent(SpotDetailUIEvent.SetReportSent(it)) },
                    setShowAttrInfoDialog = {
                        viewModel.onEvent(
                            SpotDetailUIEvent.SetShowAttrInfoDialog(
                                it
                            )
                        )
                    },
                    setAttrSelectedDialog = {
                        viewModel.onEvent(
                            SpotDetailUIEvent.SetAttrSelectedDialog(
                                it
                            )
                        )
                    },
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun SpotDetailContent(
    state: SpotDetailUIState,
    navController: NavController,
    navigateTo: (String) -> Unit,
    spotLikeClick: () -> Unit,
    updateUserLocation: (LatLng) -> Unit,
    setShowReportDialog: (Boolean) -> Unit,
    setSelectedReportOption: (String) -> Unit,
    setAdditionalReportInformation: (String) -> Unit,
    sendReportButton: () -> Unit,
    setReportSentDialog: (Boolean) -> Unit,
    setShowAttrInfoDialog: (Boolean) -> Unit,
    setAttrSelectedDialog: (SpotAttribute) -> Unit,
) {
    val scrollState = rememberScrollState()
    val showShimmer = remember { mutableStateOf(true) }
    val context = LocalContext.current

    val deepLinkToShare = generateDeepLink(screen = "spot", state.spotInfo.id)
    val shareText = stringResource(id = R.string.share_spot_text) + " $deepLinkToShare"

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                }
            }
        )

    LaunchedEffect(key1 = state.spotInfo) {
        if (state.spotInfo != Spot()) {
            showShimmer.value = false
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (state.showAttrInfoDialog) {
        AttributeInfoDialog(
            attribute = state.attrSelectedDialog,
            setShowDialog = { setShowAttrInfoDialog(it) }
        )
    }

    if (state.showReportDialog) {
        ReportDialog(
            setShowDialog = { setShowReportDialog(it) },
            dialogTitle = R.string.inform_about_spot,
            dialogDescription = R.string.inform_about_spot_description,
            availableOptions = state.availableOptionsToReport,
            setSelectedOption = { setSelectedReportOption(it) },
            selectedOptionText = state.selectedReportOption,
            additionalInformation = state.additionalReportInformation,
            setAdditionalInformation = { setAdditionalReportInformation(it) },
            buttonText = R.string.send_report,
            buttonClickedAction = {
                sendReportButton()
                setReportSentDialog(true)
            },
            reportSent = state.reportSent,
            setShowReportSent = {
                setReportSentDialog(it)
            }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, backIconButton, saveIconButton, sendIconButton, editIconButton, likeIconButton, reportIconButton) = createRefs()
            AutoSlidingCarousel(
                itemsCount = state.spotInfo.featuredImages.size,
                itemContent = { index ->
                    GlideImage(
                        model = state.spotInfo.featuredImages[index],
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
            RoundedLightBackButton(modifier = Modifier.constrainAs(backIconButton) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 24.dp)
            }, onClick = {
                navController.popBackStack()
            })
            RoundedLightSaveButton(onClick = {}, modifier = Modifier.constrainAs(saveIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
            }, isSaved = false)
            RoundedLightSendButton(modifier = Modifier.constrainAs(sendIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(saveIconButton.start, 16.dp)
            }, onClick = {
                context.startActivity(
                    getShareIntent(
                        shareText = shareText,
                        null
                    )
                )
            })
            if (state.userIsAbleToEditOrRemoveSpot) {
                RoundedLightEditButton(modifier = Modifier.constrainAs(editIconButton) {
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(sendIconButton.start, 16.dp)
                }, onClick = { navigateTo("edit_spot_screen/spotReference=${state.spotInfo.id}") })
            }
            RoundedLightReportButton(modifier = Modifier.constrainAs(reportIconButton) {
                top.linkTo(saveIconButton.bottom, 16.dp)
                end.linkTo(parent.end, 24.dp)
            }, onClick = { setShowReportDialog(true) })
            RoundedLightLikeButton(
                onClick = { spotLikeClick() },
                modifier = Modifier.constrainAs(likeIconButton) {
                    end.linkTo(parent.end, 24.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
                isLiked = state.isSpotLikedByUser
            )
        }
        //Spotter user information
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotterImage, spottedByTitle, spottedByUsername, createdDate) = createRefs()
            ProfileImage(
                imageUrl = state.spotInfo.spottedBy.image,
                size = 56.dp,
                modifier = Modifier.constrainAs(spotterImage) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(parent.top, (-16).dp)
                })
            Text(
                text = stringResource(id = R.string.spotted_by),
                style = secondaryRegularBodyS,
                modifier = Modifier.constrainAs(spottedByTitle) {
                    top.linkTo(parent.top, 4.dp)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    "@${state.spotInfo.spottedBy.username}"
                },
                style = primaryBoldHeadlineXS,
                modifier = Modifier
                    .constrainAs(spottedByUsername) {
                        top.linkTo(spottedByTitle.bottom)
                        start.linkTo(spotterImage.end, 8.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 100.dp))
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    "created ${formatDate(state.spotInfo.creationDate)}"
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
        //Spot title and description
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            val (spotTitle, spotDescription) = createRefs()
            Text(
                text = state.spotInfo.name,
                style = primaryMediumDisplayS,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(spotTitle) {
                        start.linkTo(parent.start)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 200.dp)
            )
            Text(
                text = state.spotInfo.description,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                style = secondaryRegularBodyM,
                modifier = Modifier
                    .constrainAs(spotDescription) {
                        start.linkTo(parent.start)
                        top.linkTo(spotTitle.bottom, 8.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 400.dp)
            )
        }

        //Spot information and location
        CustomSpacer(size = 8.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .background(shimmerBrush(showShimmer = showShimmer.value))
                .defaultMinSize(minWidth = 100.dp), verticalAlignment = Alignment.CenterVertically

        ) {
            if (!showShimmer.value) {
                CustomSpacer(size = 16.dp)
                Icon(imageVector = Icons.Outlined.Brightness5, contentDescription = "")
                CustomSpacer(size = 4.dp)
                Text(text = "${state.spotInfo.score}", style = secondaryRegularBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "·", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "Visited ", style = secondaryRegularBodyM)
                Text(text = "${state.spotInfo.visitedTimes} times", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "·", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(
                    text = "${state.spotLikes} " + stringResource(id = R.string.likes),
                    style = secondarySemiBoldBodyM
                )
            }
        }
        CustomSpacer(size = 8.dp)
        Text(
            text = state.spotInfo.location,
            style = secondaryRegularBodyL,
            modifier = Modifier.padding(horizontal = 24.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CustomSpacer(size = 16.dp)
        //Take me there section
        if (!showShimmer.value) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                thickness = 1.dp,
                color = Color(0xFF999999)
            )
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                val (distanceText, distance, directions) = createRefs()
                Text(
                    text = "You are only ",
                    style = secondaryRegularBodyL,
                    modifier = Modifier.constrainAs(distanceText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                Text(
                    text = "${
                        (state.userLocation.sphericalDistance(
                            LatLng(
                                state.spotInfo.locationInLatLng.latitude,
                                state.spotInfo.locationInLatLng.longitude
                            )
                        ) / 1000).roundToInt()
                    } " + stringResource(id = R.string.kms_away),
                    style = secondarySemiBoldBodyL,
                    modifier = Modifier.constrainAs(distance) {
                        start.linkTo(distanceText.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
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
                            openDirectionsOnGoogleMaps(context, state.spotInfo.locationInLatLng)
                        },
                    imageVector = Icons.Filled.Directions,
                    contentDescription = "",
                    tint = Color(0xFFFFB600)
                )
            }
            HorizontalDivider(
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
            AttributesBigListRow(
                attributesList = state.spotInfo.attributes,
                onAttributeClick = {
                    setAttrSelectedDialog(it)
                    setShowAttrInfoDialog(true)
                }
            )
            CustomSpacer(size = 24.dp)
            HorizontalDivider(
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
                Text(text = state.spotInfo.score.toString(), style = secondarySemiBoldHeadLineM)
                Text(text = " · ", style = secondarySemiBoldHeadLineM)
                Text(
                    text = "${state.spotInfo.spotReviews.size} " + stringResource(id = R.string.reviews),
                    style = secondarySemiBoldHeadLineM
                )
            }
            CustomSpacer(size = 16.dp)
            LazyRow(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(state.spotInfo.spotReviews) { _, review ->
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navigateTo("spot_review_screen/spotReference=${state.spotInfo.id}reviewReference=${review.id}") }
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
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.constrainAs(title) {
                                    top.linkTo(parent.top, 16.dp)
                                    start.linkTo(parent.start)
                                })
                            Text(
                                text = review.description,
                                style = secondaryRegularBodyM,
                                maxLines = 6,
                                overflow = TextOverflow.Ellipsis,
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
                                text = formatDate(review.creationDate),
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
                                Column {
                                    Text(
                                        text = "How it was?",
                                        style = secondarySemiBoldBodyM
                                    )
                                    AttributesSmallListRow(attributesList = review.spotAttributes)
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
                if (state.spotInfo.spotReviews.isEmpty()) {
                    LargeLightButton(
                        onClick = { navigateTo("add_spot_review_screen/spotReference=${state.spotInfo.id}") },
                        text = R.string.be_first_to_review,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    LargeLightButton(
                        onClick = { /*TODO*/ },
                        text = R.string.show_all_reviews,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                    )
                    IconButtonDark(
                        buttonIcon = Icons.Filled.Add,
                        description = R.string.add,
                        onClick = { navigateTo("add_spot_review_screen/spotReference=${state.spotInfo.id}") },
                        iconTint = Color.White
                    )
                }
            }
            CustomSpacer(size = 24.dp)
            HorizontalDivider(
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
                itemsIndexed(state.spotInfo.spotPosts) { _, post ->
                    ImagePostCard(
                        cardSize = 250.dp,
                        postInfo = post,
                        onItemClicked = { navigateTo("post_screen/postReference=${post.id}") }
                    )
                }
            }
            CustomSpacer(size = 16.dp)
            Row(
                Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (state.spotInfo.spotPosts.isEmpty()) {
                    LargeLightButton(
                        onClick = { navigateTo("add_post_screen/spotReference=${state.spotInfo.id}") },
                        text = R.string.be_first_to_post,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    LargeLightButton(
                        onClick = { /*TODO*/ },
                        text = R.string.show_all_posts,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                    )
                    IconButtonDark(
                        buttonIcon = Icons.Filled.Add,
                        description = R.string.add,
                        onClick = { navigateTo("add_post_screen/spotReference=${state.spotInfo.id}") },
                        iconTint = Color.White
                    )
                }
            }
        } else {
            //False shimmer effect when showshimmer is true
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 24.dp)
                    .background(shimmerBrush(showShimmer = showShimmer.value))
            )
            CustomSpacer(size = 16.dp)
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(20.dp)
                    .padding(horizontal = 24.dp)
                    .background(shimmerBrush(showShimmer = showShimmer.value))
            )
        }
    }

}