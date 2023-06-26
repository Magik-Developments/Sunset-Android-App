package com.madteam.sunset.ui.screens.review

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.model.SpotReview
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackVariantTitleTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.utils.getResourceId

@Composable
fun PostReviewScreen(
    viewModel: SpotReviewViewModel = hiltViewModel(),
    reviewReference: String,
    spotReference: String,
    navController: NavController
) {

    viewModel.setReviewReference(reviewReference)
    viewModel.setSpotReference(spotReference)
    val reviewInfo by viewModel.reviewInfo.collectAsState()

    Scaffold(
        topBar = {
            GoBackVariantTitleTopAppBar(title = reviewInfo.postedBy.username + " review") {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                PostReviewContent(
                    reviewInfo = reviewInfo
                )
            }
        }
    )

}

@Composable
fun PostReviewContent(
    reviewInfo: SpotReview
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Text(text = reviewInfo.title, style = primaryBoldHeadlineL, textAlign = TextAlign.Start)
        CustomSpacer(size = 40.dp)
        Text(
            text = reviewInfo.description,
            style = secondaryRegularHeadlineS,
            textAlign = TextAlign.Justify
        )
        CustomSpacer(size = 32.dp)
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(reviewInfo.spotAttributes) { _, attribute ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = getResourceId(
                                    attribute.icon,
                                    context
                                )
                            ),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = attribute.title,
                            style = secondaryRegularBodyS,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }
        CustomSpacer(size = 32.dp)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.spot_score) + " :",
                style = primaryMediumHeadlineS
            )
            CustomSpacer(size = 8.dp)
            Icon(
                imageVector = Icons.Default.Brightness7,
                contentDescription = "",
                modifier = Modifier.size(36.dp)
            )
            CustomSpacer(size = 4.dp)
            Text(text = reviewInfo.score.toString(), style = secondarySemiBoldHeadLineM)
        }
        CustomSpacer(size = 32.dp)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (userImage, userUsername, creationDate) = createRefs()
            ProfileImage(
                imageUrl = reviewInfo.postedBy.image,
                size = 60.dp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(userImage) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 16.dp)
                    })
            androidx.compose.material.Text(
                text = "@" + reviewInfo.postedBy.username,
                style = secondarySemiBoldBodyM,
                modifier = Modifier.constrainAs(userUsername) {
                    start.linkTo(userImage.end, 8.dp)
                    top.linkTo(parent.top, 16.dp)
                })
            androidx.compose.material.Text(
                text = reviewInfo.creationDate,
                style = secondaryRegularBodyM,
                color = Color(0xFF999999),
                modifier = Modifier.constrainAs(creationDate) {
                    end.linkTo(parent.end, 24.dp)
                    top.linkTo(parent.top, 16.dp)
                })
        }
    }

}

@Composable
fun PostReviewPreview(

) {

}