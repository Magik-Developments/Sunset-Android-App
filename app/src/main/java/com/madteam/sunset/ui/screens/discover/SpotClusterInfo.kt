package com.madteam.sunset.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.RoundedCloseIconButton
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.utils.shimmerBrush

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun SpotClusterInfo(
    selectedCluster: SpotClusterItem,
    onClose: (SpotClusterItem) -> Unit,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onItemClicked() },
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        elevation = 2.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val (image, title, closeButton, location, scoreIcon, score) = createRefs()
            AutoSlidingCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                itemsCount = selectedCluster.featuredImages.size,
                itemContent = { index ->
                    GlideImage(
                        model = selectedCluster.featuredImages[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
            )
            Text(
                text = selectedCluster.title,
                style = primaryBoldHeadlineS,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(image.bottom, 8.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
            )
            Text(
                text = selectedCluster.location,
                style = secondaryRegularBodyL,
                color = Color(0xFF666666),
                modifier = Modifier
                    .constrainAs(location) {
                        top.linkTo(title.bottom, 4.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
            )
            Text(
                text = "${selectedCluster.score}",
                style = secondarySemiBoldBodyM,
                modifier = Modifier
                    .constrainAs(score) {
                        end.linkTo(parent.end, 8.dp)
                        top.linkTo(scoreIcon.top)
                        bottom.linkTo(scoreIcon.bottom)
                    }
            )
            Icon(
                imageVector = Icons.Filled.Brightness7,
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(scoreIcon) {
                        end.linkTo(score.start, 4.dp)
                        top.linkTo(image.bottom, 8.dp)
                    }
            )
            RoundedCloseIconButton(
                onClick = { onClose(selectedCluster) },
                modifier = Modifier
                    .constrainAs(closeButton) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
            )
        }
    }
}
