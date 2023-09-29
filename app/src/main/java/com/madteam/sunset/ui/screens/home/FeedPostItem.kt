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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.utils.formatDate
import com.madteam.sunset.utils.generateDeepLink
import com.madteam.sunset.utils.getShareIntent

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedPostItem(
    postInfo: SpotPost,
    userInfo: UserProfile,
    postLikeClick: () -> Unit,
    onPostClicked: () -> Unit
) {

    val context = LocalContext.current

    val deepLinkToShare = generateDeepLink(screen = "post", postInfo.id)
    val shareText = stringResource(
        id = R.string.share_post_text,
        postInfo.author.username
    ) + " $deepLinkToShare"

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(370.dp),
        onClick = { onPostClicked() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (saveButton, sendButton, likeButton, clickDetailsText, clickDetailsIcon, userInformation) = createRefs()
            GlideImage(
                model = postInfo.images.first(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Card(
                modifier = Modifier
                    .size(width = 150.dp, height = 46.dp)
                    .constrainAs(userInformation) {
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start, 24.dp)
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(50.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (profileImage, username, postDate) = createRefs()
                    ProfileImage(
                        imageUrl = postInfo.author.image,
                        size = 38.dp,
                        modifier = Modifier
                            .constrainAs(profileImage) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start, 4.dp)
                            }
                    )
                    Text(
                        text = postInfo.author.username,
                        style = secondarySemiBoldBodyM,
                        modifier = Modifier
                            .constrainAs(username) {
                                start.linkTo(profileImage.end, 8.dp)
                                top.linkTo(parent.top, 8.dp)
                            }
                    )
                    Text(
                        text = formatDate(postInfo.creation_date),
                        style = secondaryRegularBodyM,
                        modifier = Modifier
                            .constrainAs(postDate) {
                                start.linkTo(profileImage.end, 8.dp)
                                bottom.linkTo(parent.bottom, 8.dp)
                            }
                    )
                }
            }
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
                onClick = { postLikeClick() },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(likeButton) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 24.dp)
                    },
                isLiked = postInfo.likedBy.contains(userInfo.username)
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
        }
    }
}
