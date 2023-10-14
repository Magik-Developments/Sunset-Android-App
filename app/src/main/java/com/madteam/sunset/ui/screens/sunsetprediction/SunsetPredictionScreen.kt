package com.madteam.sunset.ui.screens.sunsetprediction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryMediumHeadlineXS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL

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

@Composable
fun SunsetPredictionContent(

) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
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
                text = "88%",
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
                text = "Excellent",
                style = secondarySemiBoldBodyL,
                modifier = Modifier
                    .constrainAs(score) {
                        top.linkTo(scoreNumber.bottom)
                        start.linkTo(scoreNumber.start)
                        end.linkTo(scoreNumber.end)
                    }
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(scoreInfoButton) {
                        top.linkTo(score.bottom)
                        start.linkTo(score.start)
                        end.linkTo(score.end)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "What does the score means"
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