package com.madteam.sunset.ui.screens.sunsetprediction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.primaryMediumHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import kotlinx.coroutines.delay

@Composable
fun SunsetPredictionScreen(
    viewModel: SunsetPredictionViewModel = hiltViewModel(),
    navController: NavController
) {
    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController = navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SunsetPredictionContent()
            }
        }
    )
}

@Composable
fun SunsetPredictionContent(
) {
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

    LaunchedEffect(Unit) {
        scorePercentage = 88
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
            val (location, changeLocationButton, reloadInfoButton, date, scoreNumber) = createRefs()
            Text(
                text = "Terrassa, BCN",
                style = primaryBoldHeadlineM,
                modifier = Modifier
                    .constrainAs(location) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = "14 Octubre, Sabado",
                style = primaryMediumHeadlineXS,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(location.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            IconButton(
                onClick = { /*TODO*/ },
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
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(reloadInfoButton) {
                        top.linkTo(location.top)
                        bottom.linkTo(location.bottom)
                        end.linkTo(location.start)
                    }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Reload sunset info"
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
                        val (degreesText, animation) = createRefs()
                        LottieAnimation(
                            composition = sunnyIconAnimation,
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
                            text = "23º",
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
                    ) {
                        val (qualityText, animation) = createRefs()
                        val takingPhotosAnimation by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                R.raw.person_taking_photos_animation
                            )
                        )
                        LottieAnimation(
                            composition = takingPhotosAnimation,
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
                            text = "Excellent quality",
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
                val (dayLightIcon, dayLightText, dayLightTime) = createRefs()
                val (goldenHourIcon, goldenHourText, goldenHourTime) = createRefs()
                val (blueHourIcon, blueHourText, blueHourTime) = createRefs()

                //Daylight
                Box(
                    modifier = Modifier
                        .size(80.dp)
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
                    text = "Daylight",
                    style = secondaryRegularBodyM,
                    modifier = Modifier.constrainAs(dayLightText) {
                        top.linkTo(dayLightIcon.bottom)
                        start.linkTo(dayLightIcon.start)
                        end.linkTo(dayLightIcon.end)
                    }
                )
                Text(
                    text = "18:56",
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
                        .size(80.dp)
                        .constrainAs(goldenHourIcon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    LottieAnimation(
                        composition = dayLightIconAnimation,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = "Golden hour",
                    style = secondaryRegularBodyM,
                    modifier = Modifier.constrainAs(goldenHourText) {
                        top.linkTo(goldenHourIcon.bottom)
                        start.linkTo(goldenHourIcon.start)
                        end.linkTo(goldenHourIcon.end)
                    }
                )
                Text(
                    text = "19:24",
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
                    text = "Blue hour",
                    style = secondaryRegularBodyM,
                    modifier = Modifier.constrainAs(blueHourText) {
                        top.linkTo(blueHourIcon.bottom)
                        start.linkTo(blueHourIcon.start)
                        end.linkTo(blueHourIcon.end)
                    }
                )
                Text(
                    text = "19:53",
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

@Preview(showSystemUi = true)
@Composable
fun SunsetPredictionContentPreview() {
    SunsetPredictionContent()
}