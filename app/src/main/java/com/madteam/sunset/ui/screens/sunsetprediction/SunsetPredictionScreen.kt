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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.madteam.sunset.ui.theme.primaryMediumHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import kotlinx.coroutines.delay

@Composable
fun SunsetPredictionScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetPredictionContent(
) {

    val scrollState = rememberScrollState()
    var scorePercentage by remember {
        mutableIntStateOf(0)
    }
    var scoreTextVisible by remember {
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
        scoreTextVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {

            val (location, changeLocationButton, reloadInfoButton, date, backDayButton, nextDayButton, scoreNumber, score, scoreInfoButton) = createRefs()

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
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(backDayButton) {
                        top.linkTo(date.top)
                        bottom.linkTo(date.bottom)
                        end.linkTo(date.start)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "See back day prediction"
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(nextDayButton) {
                        top.linkTo(date.top)
                        bottom.linkTo(date.bottom)
                        start.linkTo(date.end)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "See next day prediction"
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(score) {
                        top.linkTo(scoreNumber.bottom)
                        start.linkTo(scoreNumber.start)
                        end.linkTo(scoreNumber.end)
                    }
            ) {
                AnimatedVisibility(visible = scoreTextVisible) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Excellent",
                            style = secondarySemiBoldBodyL,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "What does the score means"
                            )
                        }
                    }
                }
            }

        }
        CustomSpacer(size = 24.dp)
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
                val (dayLightIcon, dayLightText, dayLightTime) = createRefs()

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
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SunsetPredictionContentPreview() {
    SunsetPredictionContent()
}