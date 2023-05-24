package com.madteam.sunset.ui.screens.spotdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentReference
import com.madteam.sunset.R
import com.madteam.sunset.model.Spot
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.ImageSliderCounter
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
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM

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

@Composable
fun SpotDetailContent(
    spotInfo: Spot
) {
    Column() {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, backIconButton, saveIconButton, sendIconButton, likeIconButton, imageSliderCounter) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.sunsetstockimage),
                contentDescription = "Sunset Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
                    .constrainAs(spotImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
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
            ImageSliderCounter(
                actualImage = 1,
                totalImages = 5,
                modifier = Modifier.constrainAs(imageSliderCounter) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 16.dp)
                })
        }
        //Spotter user information
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotterImage, spottedByTitle, spottedByUsername, createdDate) = createRefs()
            ProfileImage(
                image = R.drawable.logo_degrade,
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
                text = "@SunsetApp",
                style = primaryBoldHeadlineXS,
                modifier = Modifier.constrainAs(spottedByUsername) {
                    top.linkTo(spottedByTitle.bottom)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = "created 23/04/2023",
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
                modifier = Modifier.constrainAs(spotTitle) {
                    start.linkTo(parent.start, 24.dp)
                })
            Text(
                text = spotInfo.description,
                style = secondaryRegularBodyM,
                modifier = Modifier.constrainAs(spotDescription) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(spotTitle.bottom, 8.dp)
                }
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
            Text(text = "8.8", style = secondaryRegularBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "·", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "Visited ", style = secondaryRegularBodyM)
            Text(text = "1400 times", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "·", style = secondarySemiBoldBodyM)
            CustomSpacer(size = 8.dp)
            Text(text = "800 likes", style = secondarySemiBoldBodyM)
        }
        CustomSpacer(size = 8.dp)
        Text(text = spotInfo.location, style = secondaryRegularBodyL, modifier = Modifier.padding(horizontal = 24.dp))
        //Take me there section
        //todo: take me there section
        //About this spot section
        CustomSpacer(size = 16.dp)
        Text(text = "About this spot", style = secondarySemiBoldHeadLineM, modifier = Modifier.padding(horizontal = 24.dp))
        CustomSpacer(size = 8.dp)
        Box(modifier = Modifier
            .size(80.dp)
            .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))){
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Icon(imageVector = Icons.Outlined.LocalParking, contentDescription = "")
                Text(text = "Easy to park there", style = secondaryRegularBodyS, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SpotDetailContentPreview() {
  //  SpotDetailContent(Spo)
}
