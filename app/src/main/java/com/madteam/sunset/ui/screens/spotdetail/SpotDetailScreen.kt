package com.madteam.sunset.ui.screens.spotdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.RoundedLightBackButton
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton

@Composable
fun SpotDetailScreen() {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SpotDetailContent()
            }
        }
    )
}

@Composable
fun SpotDetailContent() {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (spotImage, backIconButton, saveIconButton, sendIconButton, likeIconButton) = createRefs()
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
            end.linkTo(parent.end, 24.dp)}, isSaved = false)
        RoundedLightSendButton(modifier = Modifier.constrainAs(sendIconButton) {
            top.linkTo(parent.top, 16.dp)
            end.linkTo(saveIconButton.start, 16.dp)
        }, onClick = {})
        RoundedLightLikeButton(onClick = {}, modifier = Modifier.constrainAs(likeIconButton) {
            end.linkTo(parent.end, 24.dp)
            bottom.linkTo(parent.bottom, 16.dp)
        }, isLiked = false)
    }
}

@Preview(showSystemUi = true)
@Composable
fun SpotDetailContentPreview() {
    SpotDetailContent()
}
