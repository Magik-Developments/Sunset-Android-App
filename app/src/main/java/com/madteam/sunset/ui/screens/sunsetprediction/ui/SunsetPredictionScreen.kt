package com.madteam.sunset.ui.screens.sunsetprediction.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.LargeDarkButton
import com.madteam.sunset.ui.common.LocationPermissionDialog
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.common.SunsetPhasesInfoDialog
import com.madteam.sunset.ui.common.SunsetQualityInfoDialog
import com.madteam.sunset.ui.screens.sunsetprediction.state.SunsetPredictionUIEvent
import com.madteam.sunset.ui.screens.sunsetprediction.state.SunsetPredictionUIState
import com.madteam.sunset.ui.screens.sunsetprediction.viewmodel.SunsetPredictionViewModel
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.primaryMediumHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.convertHourToMilitaryFormat
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.hasLocationPermission
import com.madteam.sunset.utils.obtainDateOnFormat
import com.madteam.sunset.utils.shimmerBrush
import kotlinx.coroutines.delay

@Composable
fun SunsetPredictionScreen(
    viewModel: SunsetPredictionViewModel = hiltViewModel(),
    navController: NavController,
    selectedLocation: LatLng = LatLng(0.0, 0.0)
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(selectedLocation) {
        viewModel.updateUserLocation(selectedLocation)
    }

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController = navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SunsetPredictionContent(
                    state = state,
                    navigateTo = navController::navigate,
                    selectedLocation = selectedLocation,
                    setShowLocationPermissionDialog = {
                        viewModel.onEvent(
                            SunsetPredictionUIEvent.ShowLocationPermissionDialog(
                                it
                            )
                        )
                    },
                    updateUserLocation = { location ->
                        viewModel.onEvent(SunsetPredictionUIEvent.UpdateUserLocation(location))
                    },
                    setPhasesInfoDialog = { phasesInfo ->
                        viewModel.onEvent(SunsetPredictionUIEvent.SetPhasesInfoDialog(phasesInfo))
                    },
                    setQualityInfoDialog = { qualityInfo ->
                        viewModel.onEvent(SunsetPredictionUIEvent.SetQualityInfoDialog(qualityInfo))
                    },
                    setPreviousDayPrediction = { viewModel.onEvent(SunsetPredictionUIEvent.SetPreviousDayPrediction) },
                    setNextDayPrediction = { viewModel.onEvent(SunsetPredictionUIEvent.SetNextDayPrediction) },
                    retryCall = { location ->
                        viewModel.onEvent(SunsetPredictionUIEvent.UpdateUserLocation(location))
                    }
                )
            }
        }
    )
}

@Composable
fun SunsetPredictionContent(
    state: SunsetPredictionUIState,
    selectedLocation: LatLng,
    setShowLocationPermissionDialog: (Boolean) -> Unit,
    updateUserLocation: (LatLng) -> Unit,
    setPhasesInfoDialog: (String) -> Unit,
    setQualityInfoDialog: (Int) -> Unit,
    navigateTo: (String) -> Unit,
    setPreviousDayPrediction: () -> Unit,
    setNextDayPrediction: () -> Unit,
    retryCall: (LatLng) -> Unit
) {

    val context = LocalContext.current

    var permissionNotGranted by remember {
        mutableStateOf(false)
    }

    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted && selectedLocation == LatLng(0.0, 0.0)) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                } else {
                    permissionNotGranted = true
                }
            })

    LaunchedEffect(Unit) {
        if (hasLocationPermission(context)) {
            if (selectedLocation == LatLng(0.0, 0.0)) {
                getCurrentLocation(context) { lat, long ->
                    updateUserLocation(LatLng(lat, long))
                }
            }
        } else {
            setShowLocationPermissionDialog(true)
        }
    }

    if (state.showLocationPermissionDialog) {
        LocationPermissionDialog {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            setShowLocationPermissionDialog(false)
        }
    }

    if (state.phasesInfoDialog.isNotBlank()) {
        SunsetPhasesInfoDialog(
            phase = state.phasesInfoDialog,
            setShowDialog = {
                setPhasesInfoDialog("")
            }
        )
    }

    if (state.qualityInfoDialog != -1) {
        SunsetQualityInfoDialog(
            score = state.sunsetScore,
            setShowDialog = {
                setQualityInfoDialog(-1)
            })
    }

    val scrollState = rememberScrollState()
    var scorePercentage by remember {
        mutableIntStateOf(0)
    }
    var isQualityModuleVisible by remember {
        mutableStateOf(false)
    }
    val scoreNumberAnimated by animateIntAsState(
        targetValue = scorePercentage,
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing,
            delayMillis = 500
        ),
        label = "Score number animation"
    )

    LaunchedEffect(state.sunsetScore) {
        scorePercentage = state.sunsetScore
    }
    LaunchedEffect(Unit) {
        delay(4000)
        isQualityModuleVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        //Location and Date module
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            val (location, changeLocationButton, date, scoreNumber, qualityTitle, previousDay, nextDay) = createRefs()
            if (permissionNotGranted && selectedLocation.longitude == 0.0 && selectedLocation.latitude == 0.0) {
                Text(
                    text = stringResource(id = R.string.select_location),
                    style = primaryBoldHeadlineM,
                    modifier = Modifier
                        .constrainAs(location) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                IconButton(
                    onClick = {
                        navigateTo("select_location_screen/lat=${state.userLocation.latitude}long=${state.userLocation.longitude}")
                    },
                    modifier = Modifier
                        .constrainAs(changeLocationButton) {
                            top.linkTo(location.top)
                            bottom.linkTo(location.bottom)
                            start.linkTo(location.end)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Change location"
                    )
                }
            } else {
                Text(
                    text = state.userLocality,
                    style = primaryBoldHeadlineM,
                    modifier = Modifier
                        .constrainAs(location) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .defaultMinSize(minWidth = 100.dp)
                        .background(shimmerBrush(showShimmer = state.userLocality.isEmpty()))
                )
                IconButton(
                    onClick = {
                        navigateTo("select_location_screen/lat=${state.userLocation.latitude}long=${state.userLocation.longitude}")
                    },
                    modifier = Modifier
                        .constrainAs(changeLocationButton) {
                            top.linkTo(location.top)
                            bottom.linkTo(location.bottom)
                            start.linkTo(location.end)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Change location"
                    )
                }
                Text(
                    text = obtainDateOnFormat(state.informationDate),
                    style = primaryMediumHeadlineXS,
                    modifier = Modifier
                        .constrainAs(date) {
                            top.linkTo(location.bottom, 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                var previsionDay by remember {
                    mutableIntStateOf(0)
                }
                if (previsionDay > 0) {
                    IconButton(
                        onClick = {
                            setPreviousDayPrediction()
                            previsionDay--
                        },
                        modifier = Modifier
                            .constrainAs(previousDay) {
                                top.linkTo(date.top)
                                bottom.linkTo(date.bottom)
                                end.linkTo(date.start)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous day"
                        )
                    }
                }
                if (previsionDay < 2) {
                    IconButton(
                        onClick = {
                            setNextDayPrediction()
                            previsionDay++
                        },
                        modifier = Modifier
                            .constrainAs(nextDay) {
                                top.linkTo(date.top)
                                bottom.linkTo(date.bottom)
                                start.linkTo(date.end)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next day"
                        )
                    }
                }
                Text(
                    text = if (scorePercentage != -1) {
                        "$scoreNumberAnimated%"
                    } else {
                        " "
                    },
                    style = primaryBoldDisplayM,
                    fontSize = 60.sp,
                    modifier = Modifier
                        .constrainAs(scoreNumber) {
                            top.linkTo(date.bottom, 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .defaultMinSize(minWidth = 60.dp)
                        .background(shimmerBrush(showShimmer = scorePercentage == -1))
                )
                Text(
                    text = "Sunset quality score",
                    style = primaryBoldHeadlineXS,
                    modifier = Modifier
                        .constrainAs(qualityTitle) {
                            top.linkTo(scoreNumber.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
        CustomSpacer(size = 24.dp)
        //Temperature and quality sunset module
        if (permissionNotGranted && selectedLocation.longitude == 0.0 && selectedLocation.latitude == 0.0) {
            LargeDarkButton(
                onClick = {
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }, text = R.string.enable_location
            )
        } else {
            AnimatedVisibility(visible = isQualityModuleVisible && scorePercentage != -1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(150.dp)
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            val sunnyIconAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.globe_sunny_animation
                                )
                            )
                            val coldIconAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.snowman_cat_animation
                                )
                            )
                            val snowingIconAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.snowing_animation
                                )
                            )
                            val hotIconAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.melting_icecream_animation
                                )
                            )
                            val normalIconAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.cycling_animation
                                )
                            )
                            val (degreesText, animation) = createRefs()
                            LottieAnimation(
                                composition =
                                when {
                                    state.sunsetTemperature <= 0.0 -> {
                                        snowingIconAnimation
                                    }

                                    state.sunsetTemperature <= 15 -> {
                                        coldIconAnimation
                                    }

                                    state.sunsetTemperature <= 23 -> {
                                        normalIconAnimation
                                    }

                                    state.sunsetTemperature <= 30 -> {
                                        sunnyIconAnimation
                                    }

                                    state.sunsetTemperature > 30 -> {
                                        hotIconAnimation
                                    }

                                    else -> {
                                        sunnyIconAnimation
                                    }
                                },
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(80.dp)
                                    .constrainAs(animation) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                            Text(
                                text = "${state.sunsetTemperature}" + "º",
                                style = primaryBoldDisplayM,
                                modifier = Modifier.constrainAs(degreesText) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                })
                        }
                    }
                    Card(
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(150.dp)
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    setQualityInfoDialog(state.sunsetScore)
                                }
                        ) {
                            val (qualityText, animation) = createRefs()
                            val quality = when {
                                scorePercentage <= 25 -> {
                                    R.string.poor
                                }

                                scorePercentage <= 50 -> {
                                    R.string.fair
                                }

                                scorePercentage <= 75 -> {
                                    R.string.good
                                }

                                else -> {
                                    R.string.great
                                }
                            }
                            val takingPhotosAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.guy_photo_animation
                                )
                            )
                            val sadCatAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.sad_cat_animation
                                )
                            )
                            val catTvAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.cat_tv_animation
                                )
                            )
                            val dogSelfieAnimation by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.dog_selfie_animation
                                )
                            )
                            LottieAnimation(
                                composition = when {
                                    scorePercentage <= 25 -> {
                                        sadCatAnimation
                                    }

                                    scorePercentage <= 50 -> {
                                        catTvAnimation
                                    }

                                    scorePercentage <= 75 -> {
                                        dogSelfieAnimation
                                    }

                                    else -> {
                                        takingPhotosAnimation
                                    }
                                },
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(80.dp)
                                    .constrainAs(animation) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                            Text(
                                text = "${stringResource(id = quality)} ${stringResource(id = R.string.quality)}",
                                maxLines = 2,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                style = primaryBoldHeadlineS,
                                modifier = Modifier.constrainAs(qualityText) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                })
                        }
                    }
                }
            }
            CustomSpacer(size = 24.dp)
            //Sunset phases times module
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val dayLightIconAnimation by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.sun_vector_animation
                        )
                    )
                    val blueHourIconAnimation by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.moon_vector_animation
                        )
                    )
                    val goldenHourIconAnimation by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.golden_hour_animation
                        )
                    )
                    val (dayLightIcon, dayLightText, dayLightTime) = createRefs()
                    val (goldenHourIcon, goldenHourText, goldenHourTime) = createRefs()
                    val (blueHourIcon, blueHourText, blueHourTime) = createRefs()

                    //Sunset
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                setPhasesInfoDialog("sunset")
                            }
                            .constrainAs(dayLightIcon) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, 16.dp)
                            }
                    ) {
                        LottieAnimation(
                            composition = dayLightIconAnimation,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.sunset),
                        style = secondaryRegularBodyM,
                        modifier = Modifier.constrainAs(dayLightText) {
                            top.linkTo(dayLightIcon.bottom)
                            start.linkTo(dayLightIcon.start)
                            end.linkTo(dayLightIcon.end)
                        }
                    )
                    Text(
                        text = convertHourToMilitaryFormat(state.sunsetTimeInformation.results.sunset),
                        style = secondarySemiBoldHeadLineS,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .constrainAs(dayLightTime) {
                                top.linkTo(dayLightText.bottom)
                                start.linkTo(dayLightIcon.start)
                                end.linkTo(dayLightIcon.end)
                            }
                            .defaultMinSize(minWidth = 30.dp)
                            .background(shimmerBrush(showShimmer = state.sunsetTimeInformation.results.sunset.isBlank()))
                    )

                    //Golden hour
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                setPhasesInfoDialog("golden_hour")
                            }
                            .size(80.dp)
                            .constrainAs(goldenHourIcon) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition = goldenHourIconAnimation,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(50.dp),
                            alignment = Alignment.Center,
                            reverseOnRepeat = true
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.golden_hour),
                        style = secondaryRegularBodyM,
                        modifier = Modifier.constrainAs(goldenHourText) {
                            top.linkTo(goldenHourIcon.bottom)
                            start.linkTo(goldenHourIcon.start)
                            end.linkTo(goldenHourIcon.end)
                        }
                    )
                    Text(
                        text = convertHourToMilitaryFormat(state.sunsetTimeInformation.results.golden_hour),
                        style = secondarySemiBoldHeadLineS,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .constrainAs(goldenHourTime) {
                                top.linkTo(goldenHourText.bottom)
                                start.linkTo(goldenHourIcon.start)
                                end.linkTo(goldenHourIcon.end)
                            }
                            .defaultMinSize(minWidth = 30.dp)
                            .background(shimmerBrush(showShimmer = state.sunsetTimeInformation.results.golden_hour.isBlank()))
                    )

                    //Blue hour
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                setPhasesInfoDialog("blue_hour")
                            }
                            .size(80.dp)
                            .constrainAs(blueHourIcon) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end, 16.dp)
                            }
                    ) {
                        LottieAnimation(
                            composition = blueHourIconAnimation,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.blue_hour),
                        style = secondaryRegularBodyM,
                        modifier = Modifier.constrainAs(blueHourText) {
                            top.linkTo(blueHourIcon.bottom)
                            start.linkTo(blueHourIcon.start)
                            end.linkTo(blueHourIcon.end)
                        }
                    )
                    Text(
                        text = convertHourToMilitaryFormat(state.sunsetTimeInformation.results.dusk),
                        style = secondarySemiBoldHeadLineS,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .constrainAs(blueHourTime) {
                                top.linkTo(blueHourText.bottom)
                                start.linkTo(blueHourIcon.start)
                                end.linkTo(blueHourIcon.end)
                            }
                            .defaultMinSize(minWidth = 30.dp)
                            .background(shimmerBrush(showShimmer = state.sunsetTimeInformation.results.dusk.isBlank()))
                    )
                }
            }
        }
        if (state.connectionError) {
            CustomSpacer(size = 24.dp)
            SunsetButton(text = R.string.reload, onClick = { retryCall(selectedLocation) })
        }
    }
}