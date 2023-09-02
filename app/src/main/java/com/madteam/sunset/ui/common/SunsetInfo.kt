package com.madteam.sunset.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM

@Composable
fun SunsetInfoModule(
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

            val (sunsetTime, sunsetTimeIcon, remainingTime, remainingTimeDescription, exploreButton) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.sunset_illustration_01),
                contentDescription = ""
            )
            Text(
                text = "1h 30min",
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
                text = "Remaining to Sunset in your location",
                color = Color.White,
                style = primaryBoldHeadlineXS,
                modifier = Modifier
                    .constrainAs(remainingTimeDescription) {
                        start.linkTo(remainingTime.start)
                        end.linkTo(remainingTime.end)
                        top.linkTo(remainingTime.bottom)
                    }
            )
            Text(
                text = "21:06",
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
    SunsetInfoModule {}
}