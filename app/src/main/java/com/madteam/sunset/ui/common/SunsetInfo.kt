package com.madteam.sunset.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.madteam.sunset.R
import com.madteam.sunset.model.SunsetTimeResponse
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.utils.convertHourToMilitaryFormat

@Composable
fun SunsetInfoModule(
    sunsetTimeInformation: SunsetTimeResponse,
    remainingTimeToSunset: String,
    userLocality: String,
    clickToExplore: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val (sunsetTime, sunsetTimeIcon, remainingTime, remainingTimeDescription, exploreButton, poweredBy) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.sunset_illustration_01),
                contentDescription = ""
            )
            Text(
                text = remainingTimeToSunset,
                style = primaryBoldDisplayM,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(remainingTime) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom, 16.dp)
                    }
            )
            Text(
                text = if (userLocality.isNotEmpty()) {
                    "Remaining to Sunset in $userLocality"
                } else {
                    "Remaining to Sunset"
                },
                color = Color.White,
                style = primaryBoldHeadlineXS,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .constrainAs(remainingTimeDescription) {
                        start.linkTo(remainingTime.start)
                        end.linkTo(remainingTime.end)
                        top.linkTo(remainingTime.bottom)
                    }
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = convertHourToMilitaryFormat(sunsetTimeInformation.results.sunset),
                style = secondarySemiBoldBodyM,
                modifier = Modifier
                    .constrainAs(sunsetTime) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, 8.dp)
                    },
                color = Color.White
            )
            Icon(
                imageVector = Icons.Filled.WbTwilight,
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(sunsetTimeIcon) {
                        start.linkTo(sunsetTime.end)
                        top.linkTo(sunsetTime.top)
                        bottom.linkTo(sunsetTime.bottom)
                    },
                tint = Color.White
            )
            Text(
                text = "Powered by SunriseSunset.io",
                style = secondaryRegularBodyS,
                modifier = Modifier
                    .constrainAs(poweredBy) {
                        start.linkTo(parent.start, 8.dp)
                        bottom.linkTo(parent.bottom, 4.dp)
                    },
                color = Color(0xFFd9d9d9)
            )

            /*
            SmallSunsetButton(
                onClick = { clickToExplore() },
                text = R.string.explore_spots,
                modifier = Modifier
                    .constrainAs(exploreButton) {
                        top.linkTo(remainingTimeDescription.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            TODO: Do the navigation correctly
             */

        }
    }
}

@Preview
@Composable
fun SunsetInfoModulePreview() {
    //SunsetInfoModule(SunsetTimeResponse("21:06", "34:56", "65:34")) {}
}