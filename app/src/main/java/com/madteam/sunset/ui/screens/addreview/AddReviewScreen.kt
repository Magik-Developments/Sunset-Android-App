package com.madteam.sunset.ui.screens.addreview

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomDivider
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.common.ScoreSlider
import com.madteam.sunset.ui.theme.primaryBoldDisplayS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.getResourceId

private const val MAX_CHAR_LENGTH_REVIEW_TITLE = 50
private const val MAX_CHAR_LENGTH_REVIEW_DESCRIPTION = 2500
const val FAVORABLE_ATTRIBUTES = "favorable"
const val NON_FAVORABLE_ATTRIBUTES = "non-favorable"
const val LOCATION_ATTRIBUTES = "location"
const val SUNSET_ATTRIBUTES = "sunset"

@Composable
fun AddReviewScreen(
    spotReference: String,
    viewModel: AddReviewViewModel = hiltViewModel(),
    navController: NavController
) {

    val attributesList by viewModel.attributesList.collectAsStateWithLifecycle()
    val selectedAttributes by viewModel.selectedAttributes.collectAsStateWithLifecycle()
    val reviewTitle by viewModel.reviewTitle.collectAsStateWithLifecycle()
    val reviewDescription by viewModel.reviewDescription.collectAsStateWithLifecycle()
    val reviewScore by viewModel.reviewScore.collectAsStateWithLifecycle()
    val showExitDialog by viewModel.showExitDialog.collectAsStateWithLifecycle()
    val showFinishedDialog by viewModel.showFinishedDialog.collectAsStateWithLifecycle()
    val isReadyToPost =
        selectedAttributes.isNotEmpty() && reviewTitle.isNotEmpty() && reviewDescription.isNotEmpty()
    val uploadProgress by viewModel.uploadProgress.collectAsStateWithLifecycle()
    val errorToastText by viewModel.errorToastText.collectAsStateWithLifecycle()

    BackHandler {
        if (isReadyToPost) {
            viewModel.setShowExitDialog(true)
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_review,
                onQuitClick = {
                    if (isReadyToPost) {
                        viewModel.setShowExitDialog(true)
                    } else {
                        navController.popBackStack()
                    }
                },
                onContinueClick = {
                    viewModel.createNewReview(spotReference)
                },
                canContinue = isReadyToPost
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddReviewContent(
                    attributesList = attributesList,
                    onAttributeClicked = viewModel::modifySelectedAttributes,
                    selectedAttributes = selectedAttributes,
                    reviewTitle = reviewTitle,
                    reviewDescription = reviewDescription,
                    onReviewDescriptionChanged = viewModel::modifyReviewDescription,
                    onReviewTitleChanged = viewModel::modifyReviewTitle,
                    onReviewScoreChanged = viewModel::modifyReviewScore,
                    reviewScore = reviewScore,
                    showExitDialog = showExitDialog,
                    spotReference = spotReference,
                    setShowExitDialog = viewModel::setShowExitDialog,
                    exitAddReview = navController::popBackStack,
                    uploadProgress = uploadProgress,
                    errorToast = errorToastText,
                    navigateTo = navController::navigate,
                    clearUploadProgress = viewModel::clearUpdateProgressState,
                    clearErrorToast = viewModel::clearErrorToastText,
                    showFinishedDialog = showFinishedDialog,
                    setShowFinishedDialog = viewModel::setShowFinishedDialog
                )
            }
        }
    )
}

@Composable
fun AddReviewContent(
    attributesList: List<SpotAttribute>,
    selectedAttributes: List<SpotAttribute>,
    reviewTitle: String,
    reviewDescription: String,
    reviewScore: Int,
    showExitDialog: Boolean,
    uploadProgress: Resource<String>,
    errorToast: String,
    spotReference: String,
    clearUploadProgress: () -> Unit,
    clearErrorToast: () -> Unit,
    onReviewTitleChanged: (String) -> Unit,
    onReviewDescriptionChanged: (String) -> Unit,
    onAttributeClicked: (SpotAttribute) -> Unit,
    onReviewScoreChanged: (Float) -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    navigateTo: (String) -> Unit,
    exitAddReview: () -> Unit,
    showFinishedDialog: Boolean,
    setShowFinishedDialog: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (errorToast.isNotBlank()) {
        Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show()
        clearErrorToast()
    }

    if (showExitDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowExitDialog(it) },
            dialogTitle = R.string.are_you_sure,
            dialogDescription = R.string.exit_post_dialog,
            positiveButtonText = R.string.discard_changes,
            dismissButtonText = R.string.cancel,
            dismissClickedAction = { setShowExitDialog(false) },
            positiveClickedAction = {
                setShowExitDialog(false)
                exitAddReview()
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 24.dp, top = 24.dp, bottom = 24.dp)
            .verticalScroll(scrollState)
    ) {
        CustomTextField(
            value = reviewTitle,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_REVIEW_TITLE) {
                    onReviewTitleChanged(it)
                } else {
                    Toast.makeText(context, R.string.max_characters_reached, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            hint = R.string.add_title_review,
            textStyle = secondarySemiBoldHeadLineS,
            textColor = Color(0xFF666666),
            maxLines = 2
        )
        CustomTextField(
            value = reviewDescription,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_REVIEW_DESCRIPTION) {
                    onReviewDescriptionChanged(it)
                } else {
                    Toast.makeText(context, R.string.max_characters_reached, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            hint = R.string.add_description_review,
            textStyle = secondaryRegularHeadlineS,
            textColor = Color(0xFF666666),
            maxLines = 6
        )
        CustomSpacer(size = 16.dp)
        CustomDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            color = Color(0xFF999999)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.how_it_was),
            style = primaryBoldHeadlineL,
            modifier = Modifier.padding(start = 16.dp)
        )
        CustomSpacer(size = 4.dp)
        Text(
            text = stringResource(id = R.string.add_attributes_review),
            style = secondarySemiBoldBodyM,
            modifier = Modifier.padding(start = 16.dp)
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.good_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(attributesList.filter { it.type == FAVORABLE_ATTRIBUTES }) { _, attribute ->
                val isSelected = selectedAttributes.contains(attribute)
                val customBackgroundColor = if (isSelected) Color(0x80FFB600) else Color.White
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                        .background(customBackgroundColor, RoundedCornerShape(20.dp))
                        .clickable { onAttributeClicked(attribute) }

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
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.bad_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(attributesList.filter { it.type == NON_FAVORABLE_ATTRIBUTES }) { _, attribute ->
                val isSelected = selectedAttributes.contains(attribute)
                val customBackgroundColor = if (isSelected) Color(0x80FFB600) else Color.White
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                        .background(customBackgroundColor, RoundedCornerShape(20.dp))
                        .clickable { onAttributeClicked(attribute) }
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
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.sunset_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(attributesList.filter { it.type == SUNSET_ATTRIBUTES }) { _, attribute ->
                val isSelected = selectedAttributes.contains(attribute)
                val customBackgroundColor = if (isSelected) Color(0x80FFB600) else Color.White
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                        .background(customBackgroundColor, RoundedCornerShape(20.dp))
                        .clickable { onAttributeClicked(attribute) }
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
        CustomDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            color = Color(0xFF999999)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.review_score),
            style = primaryBoldHeadlineL,
            modifier = Modifier.padding(start = 16.dp)
        )
        CustomSpacer(size = 4.dp)
        Text(
            text = stringResource(id = R.string.add_review_score),
            style = secondarySemiBoldBodyM,
            modifier = Modifier.padding(start = 16.dp)
        )
        ScoreSlider(
            value = reviewScore.toFloat(),
            onValueChange = { onReviewScoreChanged(it) },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (reviewScore < 3) {
                    Icons.Outlined.BrightnessLow
                } else if (reviewScore < 5) {
                    Icons.Outlined.Brightness4
                } else if (reviewScore < 7) {
                    Icons.Outlined.Brightness5
                } else if (reviewScore < 9) {
                    Icons.Outlined.Brightness6
                } else if (reviewScore > 9) {
                    Icons.Outlined.Brightness7
                } else {
                    Icons.Outlined.BrightnessLow
                },
                contentDescription = "Score icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
            )
            CustomSpacer(size = 8.dp)
            Text(text = reviewScore.toString(), style = primaryBoldDisplayS)
        }
        CustomSpacer(size = 24.dp)
    }

    if (showFinishedDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowFinishedDialog(it) },
            dialogTitle = R.string.post_review_finished,
            dialogDescription = R.string.post_review_finished_description,
            dismissButtonText = R.string.continue_text,
            dismissClickedAction = { navigateTo(SunsetRoutes.DiscoverScreen.route) }
        )
    }

    when (uploadProgress) {
        is Resource.Loading -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .pointerInput(Unit) {}
                ) {
                    CircularLoadingDialog()
                }
            }
        }

        is Resource.Success -> {
            if (uploadProgress.data != "") {
                LaunchedEffect(key1 = uploadProgress.data) {
                    setShowFinishedDialog(true)
                    clearUploadProgress()
                }
            } else if (uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            }
        }

        else -> {}
    }

}
