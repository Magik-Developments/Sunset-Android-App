package com.madteam.sunset.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.SpotReview
import com.madteam.sunset.ui.common.AttributeInfoDialog
import com.madteam.sunset.ui.common.AttributesBigListRow
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackVariantTitleTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.utils.formatDate

@Composable
fun PostReviewScreen(
    viewModel: SpotReviewViewModel = hiltViewModel(),
    reviewReference: String,
    spotReference: String,
    navController: NavController
) {

    viewModel.setReferences(reviewReference, spotReference)
    val reviewInfo by viewModel.reviewInfo.collectAsState()
    val showAttrInfoDialog by viewModel.showAttrInfoDialog.collectAsStateWithLifecycle()
    val selectedAttrInfoDialog by viewModel.selectedAttrInfoDialog.collectAsStateWithLifecycle()

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
                    reviewInfo = reviewInfo,
                    setAttrSelectedDialog = viewModel::setSelectedAttrInfoDialog,
                    setShowAttrInfoDialog = viewModel::setShowAttrInfoDialog,
                    showAttrInfoDialog = showAttrInfoDialog,
                    selectedAttrInfoDialog = selectedAttrInfoDialog
                )
            }
        }
    )

}

@Composable
fun PostReviewContent(
    reviewInfo: SpotReview,
    setAttrSelectedDialog: (SpotAttribute) -> Unit,
    setShowAttrInfoDialog: (Boolean) -> Unit,
    showAttrInfoDialog: Boolean,
    selectedAttrInfoDialog: SpotAttribute
) {

    if (showAttrInfoDialog) {
        AttributeInfoDialog(
            attribute = selectedAttrInfoDialog,
            setShowDialog = { setShowAttrInfoDialog(it) }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 24.dp)
        Text(
            text = reviewInfo.title,
            style = primaryBoldHeadlineL,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(end = 48.dp)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = reviewInfo.description,
            style = secondaryRegularHeadlineS,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(end = 24.dp)
        )
        CustomSpacer(size = 24.dp)
        AttributesBigListRow(
            attributesList = reviewInfo.spotAttributes,
            onAttributeClick = {
                setAttrSelectedDialog(it)
                setShowAttrInfoDialog(true)
            }
        )
        CustomSpacer(size = 24.dp)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.spot_score) + " :",
                style = secondarySemiBoldBodyL
            )
            CustomSpacer(size = 8.dp)
            Icon(
                imageVector = Icons.Default.Brightness7,
                contentDescription = ""
            )
            CustomSpacer(size = 4.dp)
            Text(text = reviewInfo.score.toString(), style = secondarySemiBoldHeadLineM)
        }
        CustomSpacer(size = 16.dp)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reviewed by",
                style = secondarySemiBoldBodyL,
                modifier = Modifier.padding(start = 16.dp)
            )
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
                        .constrainAs(userImage) {
                            start.linkTo(parent.start, 16.dp)
                            top.linkTo(parent.top, 16.dp)
                            bottom.linkTo(parent.bottom, 16.dp)
                        })
                androidx.compose.material.Text(
                    text = "@" + reviewInfo.postedBy.username,
                    style = secondarySemiBoldBodyM,
                    modifier = Modifier.constrainAs(userUsername) {
                        start.linkTo(userImage.end, 8.dp)
                        top.linkTo(userImage.top, 16.dp)
                        bottom.linkTo(userImage.bottom, 36.dp)
                    })
                if (reviewInfo.creationDate.isNotBlank()) {
                    androidx.compose.material.Text(
                        text = formatDate(reviewInfo.creationDate),
                        style = secondaryRegularBodyM,
                        color = Color(0xFF999999),
                        modifier = Modifier.constrainAs(creationDate) {
                            start.linkTo(userImage.end, 8.dp)
                            top.linkTo(userImage.top, 16.dp)
                            bottom.linkTo(userImage.bottom)
                        })
                }
            }
        }

    }

}

@Preview
@Composable
fun PostReviewPreview(

) {

}