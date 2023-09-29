package com.madteam.sunset.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.madteam.sunset.R
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.utils.generateDeepLink
import com.madteam.sunset.utils.getShareIntent

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedSpotItem(
    spotInfo: Spot,
    userInfo: UserProfile,
    spotLikeClick: () -> Unit,
    onSpotClicked: () -> Unit
) {

    val context = LocalContext.current

    val deepLinkToShare = generateDeepLink(screen = "spot", spotInfo.id)
    val shareText = stringResource(id = R.string.share_spot_text) + " $deepLinkToShare"

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(370.dp),
        onClick = { onSpotClicked() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (saveButton, sendButton, likeButton, clickDetailsText, clickDetailsIcon, titleInformation, subtitleInformation) = createRefs()
            GlideImage(
                model = spotInfo.featuredImages.first(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = 4000f
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {}
            RoundedLightSaveButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(saveButton) {
                        top.linkTo(parent.top, 16.dp)
                        end.linkTo(parent.end, 24.dp)
                    }
            )
            RoundedLightLikeButton(
                onClick = { spotLikeClick() },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(likeButton) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 24.dp)
                    },
                isLiked = spotInfo.likedBy.contains(userInfo.username)
            )
            RoundedLightSendButton(
                onClick = {
                    context.startActivity(
                        getShareIntent(
                            shareText = shareText,
                            null
                        )
                    )
                },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(sendButton) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )
            Text(
                text = stringResource(id = R.string.click_to_see_details),
                style = secondaryRegularBodyM,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(clickDetailsText) {
                        bottom.linkTo(parent.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .constrainAs(clickDetailsIcon) {
                        bottom.linkTo(clickDetailsText.top)
                        start.linkTo(clickDetailsText.start)
                        end.linkTo(clickDetailsText.end)
                    }
            )
            Text(
                text = spotInfo.location,
                style = secondaryRegularBodyM,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(subtitleInformation) {
                        bottom.linkTo(sendButton.top, 24.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )
            Text(
                text = spotInfo.name,
                style = secondarySemiBoldBodyL,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(titleInformation) {
                        bottom.linkTo(subtitleInformation.top, 4.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )
        }
    }
}

@Preview
@Composable
fun FeedPostItemPreview() {
}