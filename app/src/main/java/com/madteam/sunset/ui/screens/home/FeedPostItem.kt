package com.madteam.sunset.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL

@Composable
fun FeedPostItem() {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(380.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (saveButton, userInformation, sendButton, likeButton, clickDetailsText, clickDetailsIcon, titleInformation, subtitleInformation) = createRefs()
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
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(likeButton) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 24.dp)
                    },
                isLiked = false
            )
            RoundedLightSendButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .constrainAs(sendButton) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )
            Text(
                text = "Click to see details",
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
                contentDescription = "Click to see details icon",
                tint = Color.White,
                modifier = Modifier
                    .constrainAs(clickDetailsIcon) {
                        bottom.linkTo(clickDetailsText.top)
                        start.linkTo(clickDetailsText.start)
                        end.linkTo(clickDetailsText.end)
                    }
            )
            Text(
                text = "Terrassa, Barcelona.",
                style = secondaryRegularBodyM,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(subtitleInformation) {
                        bottom.linkTo(sendButton.top, 24.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )
            Text(
                text = "La piscina de vallparadís",
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
    FeedPostItem()
}