package com.madteam.sunset.ui.screens.spotdetail

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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.madteam.sunset.model.Spot
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
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.primaryMediumDisplayS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.utils.formatDate
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.getResourceId
import com.madteam.sunset.utils.openDirectionsOnGoogleMaps
import com.madteam.sunset.utils.shimmerBrush
import kotlin.math.roundToInt

@Composable
fun SpotDetailScreen(
    navController: NavController,
    viewModel: SpotDetailViewModel = hiltViewModel(),
    spotReference: String
) {

    viewModel.setSpotReference("spots/$spotReference")
    val spotInfo by viewModel.spotInfo.collectAsStateWithLifecycle()
    val isSpotLikedByUser by viewModel.spotIsLiked.collectAsStateWithLifecycle()
    val spotLikes by viewModel.spotLikes.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val isUserAdmin by viewModel.userIsAbleToEditOrRemoveSpot.collectAsStateWithLifecycle()
    val showReportDialog by viewModel.showReportDialog.collectAsStateWithLifecycle()
    val availableOptionsToReport by viewModel.availableOptionsToReport.collectAsStateWithLifecycle()
    val selectedReportOption by viewModel.selectedReportOption.collectAsStateWithLifecycle()
    val additionalReportInformation by viewModel.additionalReportInformation.collectAsStateWithLifecycle()
    val reportSentDialog by viewModel.showReportSentDialog.collectAsStateWithLifecycle()

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SpotDetailContent(
                    spotInfo = spotInfo,
                    navigateTo = navController::navigate,
                    goBack = navController::popBackStack,
                    spotLikeClick = viewModel::modifyUserSpotLike,
                    spotLikedByUser = isSpotLikedByUser,
                    spotLikes = spotLikes,
                    updateUserLocation = viewModel::updateUserLocation,
                    userLocation = userLocation,
                    isUserAdmin = isUserAdmin,
                    showReportDialog = showReportDialog,
                    setShowReportDialog = viewModel::setShowReportDialog,
                    availableOptionsToReport = availableOptionsToReport,
                    setSelectedReportOption = viewModel::selectedReportOption,
                    selectedReportOption = selectedReportOption,
                    additionalReportInformation = additionalReportInformation,
                    setAdditionalReportInformation = viewModel::setAdditionalReportInformation,
                    sendReportButton = viewModel::sendReportIntent,
                    setReportSentDialog = viewModel::setReportSentDialog,
                    reportSentDialog = reportSentDialog
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun SpotDetailContent(
    spotInfo: Spot,
    navigateTo: (String) -> Unit,
    goBack: () -> Unit,
    spotLikeClick: () -> Unit,
    spotLikedByUser: Boolean,
    spotLikes: Int,
    updateUserLocation: (LatLng) -> Unit,
    userLocation: LatLng,
    isUserAdmin: Boolean,
    showReportDialog: Boolean,
    setShowReportDialog: (Boolean) -> Unit,
    availableOptionsToReport: List<String>,
    setSelectedReportOption: (String) -> Unit,
    selectedReportOption: String,
    additionalReportInformation: String,
    setAdditionalReportInformation: (String) -> Unit,
    sendReportButton: () -> Unit,
    setReportSentDialog: (Boolean) -> Unit,
    reportSentDialog: Boolean
) {
    val scrollState = rememberScrollState()
    val showShimmer = remember { mutableStateOf(true) }
    val context = LocalContext.current

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

    LaunchedEffect(key1 = spotInfo) {
        if (spotInfo != Spot()) {
            showShimmer.value = false
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (showReportDialog) {
        ReportDialog(
            setShowDialog = { setShowReportDialog(it) },
            dialogTitle = R.string.inform_about_spot,
            dialogDescription = R.string.inform_about_spot_description,
            availableOptions = availableOptionsToReport,
            setSelectedOption = { setSelectedReportOption(it) },
            selectedOptionText = selectedReportOption,
            additionalInformation = additionalReportInformation,
            setAdditionalInformation = { setAdditionalReportInformation(it) },
            buttonText = R.string.send_report,
            buttonClickedAction = {
                sendReportButton()
                setReportSentDialog(true)
            },
            reportSent = reportSentDialog,
            setShowReportSent = {
                setReportSentDialog(it)
            }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, backIconButton, saveIconButton, sendIconButton, editIconButton, likeIconButton, reportIconButton) = createRefs()
            AutoSlidingCarousel(
                itemsCount = spotInfo.featuredImages.size,
                itemContent = { index ->
                    GlideImage(
                        model = spotInfo.featuredImages[index],
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
            }, onClick = { goBack() })
            RoundedLightSaveButton(onClick = {}, modifier = Modifier.constrainAs(saveIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
            }, isSaved = false)
            RoundedLightSendButton(modifier = Modifier.constrainAs(sendIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(saveIconButton.start, 16.dp)
            }, onClick = {})
            if (isUserAdmin) {
                RoundedLightEditButton(modifier = Modifier.constrainAs(editIconButton) {
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(sendIconButton.start, 16.dp)
                }, onClick = { navigateTo("edit_spot_screen/spotReference=${spotInfo.id}") })
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
                isLiked = spotLikedByUser
            )
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
                text = if (showShimmer.value) {
                    ""
                } else {
                    "@${spotInfo.spottedBy.username}"
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
                    "created ${formatDate(spotInfo.creationDate)}"
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
                text = spotInfo.name,
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
                text = spotInfo.description,
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
                Text(text = "${spotInfo.score}", style = secondaryRegularBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "·", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "Visited ", style = secondaryRegularBodyM)
                Text(text = "${spotInfo.visitedTimes} times", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "·", style = secondarySemiBoldBodyM)
                CustomSpacer(size = 8.dp)
                Text(text = "$spotLikes likes", style = secondarySemiBoldBodyM)
            }
        }
        CustomSpacer(size = 8.dp)
        Text(
            text = spotInfo.location,
            style = secondaryRegularBodyL,
            modifier = Modifier.padding(horizontal = 24.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CustomSpacer(size = 16.dp)
        //Take me there section
        if (!showShimmer.value) {
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
                        (userLocation.sphericalDistance(
                            LatLng(
                                spotInfo.locationInLatLng.latitude,
                                spotInfo.locationInLatLng.longitude
                            )
                        ) / 1000).roundToInt()
                    } kms away",
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
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
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
                Text(
                    text = "${spotInfo.spotReviews.size} reviews",
                    style = secondarySemiBoldHeadLineM
                )
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
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navigateTo("spot_review_screen/spotReference=${spotInfo.id}reviewReference=${review.id}") }
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
                if (spotInfo.spotReviews.isEmpty()) {
                    LargeLightButton(
                        onClick = { navigateTo("add_spot_review_screen/spotReference=${spotInfo.id}") },
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
                        onClick = { navigateTo("add_spot_review_screen/spotReference=${spotInfo.id}") },
                        iconTint = Color.White
                    )
                }
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
                if (spotInfo.spotPosts.isEmpty()) {
                    LargeLightButton(
                        onClick = { navigateTo("add_post_screen/spotReference=${spotInfo.id}") },
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
                        onClick = { navigateTo("add_post_screen/spotReference=${spotInfo.id}") },
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