package com.madteam.sunset.ui.screens.sunsetprediction

import android.Manifest
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.LocationPermissionDialog
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetPhasesInfoDialog
import com.madteam.sunset.ui.common.SunsetQualityInfoDialog
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

    val showLocationPermissionDialog by viewModel.showLocationPermissionDialog.collectAsStateWithLifecycle()
    val userLocality by viewModel.userLocality.collectAsStateWithLifecycle()
    val sunsetTimeInformation by viewModel.sunsetTimeInformation.collectAsStateWithLifecycle()
    val phasesInfoDialog by viewModel.phasesInfoDialog.collectAsStateWithLifecycle()
    val sunsetScore by viewModel.sunsetScore.collectAsStateWithLifecycle()
    val sunsetTemperature by viewModel.sunsetTemperature.collectAsStateWithLifecycle()
    val qualityInfoDialog by viewModel.qualityInfoDialog.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()

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
                    showLocationPermissionDialog = showLocationPermissionDialog,
                    setShowLocationPermissionDialog = viewModel::showLocationPermissionDialog,
                    updateUserLocation = viewModel::updateUserLocation,
                    userLocality = userLocality,
                    sunsetTimeInformation = sunsetTimeInformation,
                    setPhasesInfoDialog = viewModel::setPhasesInfoDialog,
                    phasesInfoDialog = phasesInfoDialog,
                    sunsetScore = sunsetScore,
                    sunsetTemperature = sunsetTemperature,
                    qualityInfoDialog = qualityInfoDialog,
                    setQualityInfoDialog = viewModel::setQualityInfoDialog,
                    navigateTo = navController::navigate,
                    userLocation = userLocation,
                    selectedLocation = selectedLocation
                )
            }
        }
    )
}

@Composable
fun SunsetPredictionContent(
    showLocationPermissionDialog: Boolean,
    setShowLocationPermissionDialog: (Boolean) -> Unit,
    updateUserLocation: (LatLng) -> Unit,
    userLocality: String,
    sunsetTimeInformation: SunsetTimeResponse,
    setPhasesInfoDialog: (String) -> Unit,
    phasesInfoDialog: String,
    sunsetScore: Int,
    sunsetTemperature: Double,
    qualityInfoDialog: Int,
    setQualityInfoDialog: (Int) -> Unit,
    navigateTo: (String) -> Unit,
    userLocation: LatLng,
    selectedLocation: LatLng
) {

    val context = LocalContext.current

    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted && selectedLocation == LatLng(0.0, 0.0)) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                } else {
                    //TODO: Not granted
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

    if (showLocationPermissionDialog) {
        LocationPermissionDialog {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            setShowLocationPermissionDialog(false)
        }
    }

    if (phasesInfoDialog.isNotBlank()) {
        SunsetPhasesInfoDialog(
            phase = phasesInfoDialog,
            setShowDialog = {
                setPhasesInfoDialog("")
            }
        )
    }

    if (qualityInfoDialog != -1) {
        SunsetQualityInfoDialog(
            score = sunsetScore,
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

    LaunchedEffect(sunsetScore) {
        scorePercentage = sunsetScore
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
            val (location, changeLocationButton, date, scoreNumber, qualityTitle) = createRefs()
            Text(
                text = userLocality,
                style = primaryBoldHeadlineM,
                modifier = Modifier
                    .constrainAs(location) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .background(shimmerBrush(showShimmer = userLocality.isEmpty()))
            )
            Text(
                text = obtainDateOnFormat(),
                style = primaryMediumHeadlineXS,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(location.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            IconButton(
                onClick = {
                    navigateTo("select_location_screen/lat=${userLocation.latitude}long=${userLocation.longitude}")
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
                text = "$scoreNumberAnimated%",
                style = primaryBoldDisplayM,
                fontSize = 60.sp,
                modifier = Modifier
                    .constrainAs(scoreNumber) {
                        top.linkTo(date.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
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
        CustomSpacer(size = 24.dp)
        //Temperature and quality sunset module
        AnimatedVisibility(visible = isQualityModuleVisible) {
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
                            if (sunsetTemperature <= 0.0) {
                                snowingIconAnimation
                            } else if (sunsetTemperature <= 15) {
                                coldIconAnimation
                            } else if (sunsetTemperature <= 23) {
                                normalIconAnimation
                            } else if (sunsetTemperature <= 30) {
                                sunnyIconAnimation
                            } else if (sunsetTemperature > 30) {
                                hotIconAnimation
                            } else {
                                sunnyIconAnimation
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
                            text = "$sunsetTemperature" + "º",
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
                                setQualityInfoDialog(sunsetScore)
                            }
                    ) {
                        val (qualityText, animation) = createRefs()
                        val quality = if (scorePercentage <= 25) {
                            R.string.poor
                        } else if (scorePercentage <= 50) {
                            R.string.fair
                        } else if (scorePercentage <= 75) {
                            R.string.good
                        } else {
                            R.string.great
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
                            composition = if (scorePercentage <= 25) {
                                sadCatAnimation
                            } else if (scorePercentage <= 50) {
                                catTvAnimation
                            } else if (scorePercentage <= 75) {
                                dogSelfieAnimation
                            } else {
                                takingPhotosAnimation
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

                //Daylight
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            setPhasesInfoDialog("daylight")
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
                    text = stringResource(id = R.string.daylight),
                    style = secondaryRegularBodyM,
                    modifier = Modifier.constrainAs(dayLightText) {
                        top.linkTo(dayLightIcon.bottom)
                        start.linkTo(dayLightIcon.start)
                        end.linkTo(dayLightIcon.end)
                    }
                )
                Text(
                    text = convertHourToMilitaryFormat(sunsetTimeInformation.results.sunset),
                    style = secondarySemiBoldHeadLineS,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .constrainAs(dayLightTime) {
                            top.linkTo(dayLightText.bottom)
                            start.linkTo(dayLightIcon.start)
                            end.linkTo(dayLightIcon.end)
                        }
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
                    text = convertHourToMilitaryFormat(sunsetTimeInformation.results.golden_hour),
                    style = secondarySemiBoldHeadLineS,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .constrainAs(goldenHourTime) {
                            top.linkTo(goldenHourText.bottom)
                            start.linkTo(goldenHourIcon.start)
                            end.linkTo(goldenHourIcon.end)
                        }
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
                    text = convertHourToMilitaryFormat(sunsetTimeInformation.results.dusk),
                    style = secondarySemiBoldHeadLineS,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .constrainAs(blueHourTime) {
                            top.linkTo(blueHourText.bottom)
                            start.linkTo(blueHourIcon.start)
                            end.linkTo(blueHourIcon.end)
                        }
                )
            }
        }
    }
}