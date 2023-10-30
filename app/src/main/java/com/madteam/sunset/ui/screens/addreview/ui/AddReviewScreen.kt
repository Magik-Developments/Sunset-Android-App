package com.madteam.sunset.ui.screens.addreview.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AttributesBigListSelectable
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomDivider
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.ScoreSlider
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.screens.addreview.state.AddReviewUIEvent
import com.madteam.sunset.ui.screens.addreview.state.AddReviewUIState
import com.madteam.sunset.ui.screens.addreview.viewmodel.AddReviewViewModel
import com.madteam.sunset.ui.theme.primaryBoldDisplayS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.Resource

private const val MAX_CHAR_LENGTH_REVIEW_TITLE = 50
private const val MAX_CHAR_LENGTH_REVIEW_DESCRIPTION = 2500
const val FAVORABLE_ATTRIBUTES = "favorable"
const val NON_FAVORABLE_ATTRIBUTES = "non-favorable"
const val SUNSET_ATTRIBUTES = "sunset"

@Composable
fun AddReviewScreen(
    spotReference: String,
    viewModel: AddReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isReadyToPost =
        state.selectedAttributes.isNotEmpty() &&
                state.reviewTitle.isNotEmpty() &&
                state.reviewDescription.isNotEmpty()

    BackHandler {
        if (isReadyToPost) {
            viewModel.onEvent(AddReviewUIEvent.ShowExitDialog(true))
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            GoBackTopAppBar(
                title = R.string.add_review,
                onClick = {
                    if (isReadyToPost) {
                        viewModel.onEvent(AddReviewUIEvent.ShowExitDialog(true))
                    } else {
                        navController.popBackStack()
                    }
                },
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddReviewContent(
                    state = state,
                    canContinue = isReadyToPost,
                    exitAddReview = navController::popBackStack,
                    navController = navController,
                    onAttributeClicked = {
                        viewModel.onEvent(
                            AddReviewUIEvent.UpdateSelectedAttribute(
                                it
                            )
                        )
                    },
                    onReviewDescriptionChanged =
                    { viewModel.onEvent(AddReviewUIEvent.UpdateReviewDescription(it)) },
                    onReviewTitleChanged = { viewModel.onEvent(AddReviewUIEvent.UpdateReviewTitle(it)) },
                    onReviewScoreChanged = { viewModel.onEvent(AddReviewUIEvent.UpdateReviewScore(it)) },
                    setShowExitDialog = { viewModel.onEvent(AddReviewUIEvent.ShowExitDialog(it)) },

                    clearUploadProgress = { viewModel.onEvent(AddReviewUIEvent.ClearUploadProgress) },
                    clearErrorToast = { viewModel.onEvent(AddReviewUIEvent.ClearErrorToastText) },
                    setShowFinishedDialog =
                    { viewModel.onEvent(AddReviewUIEvent.ShowFinishedDialog(it)) },
                    onContinueClick = {
                        viewModel.onEvent(
                            AddReviewUIEvent.CreateNewReview(
                                spotReference
                            )
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun AddReviewContent(
    state: AddReviewUIState,
    clearUploadProgress: () -> Unit,
    clearErrorToast: () -> Unit,
    onReviewTitleChanged: (String) -> Unit,
    onReviewDescriptionChanged: (String) -> Unit,
    onAttributeClicked: (SpotAttribute) -> Unit,
    onReviewScoreChanged: (Float) -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    navController: NavController,
    exitAddReview: () -> Unit,
    setShowFinishedDialog: (Boolean) -> Unit,
    canContinue: Boolean,
    onContinueClick: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (state.errorToastText != -1) {
        Toast.makeText(context, stringResource(state.errorToastText), Toast.LENGTH_SHORT).show()
        clearErrorToast()
    }

    if (state.showExitDialog) {
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
            value = state.reviewTitle,
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
            value = state.reviewDescription,
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
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = FAVORABLE_ATTRIBUTES
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.bad_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = NON_FAVORABLE_ATTRIBUTES
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.sunset_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = SUNSET_ATTRIBUTES
        )
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
            value = state.reviewScore.toFloat(),
            onValueChange = { onReviewScoreChanged(it) },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = when {
                    state.reviewScore < 3 -> {
                        Icons.Outlined.BrightnessLow
                    }

                    state.reviewScore < 5 -> {
                        Icons.Outlined.Brightness4
                    }

                    state.reviewScore < 7 -> {
                        Icons.Outlined.Brightness5
                    }

                    state.reviewScore < 9 -> {
                        Icons.Outlined.Brightness6
                    }

                    state.reviewScore > 9 -> {
                        Icons.Outlined.Brightness7
                    }

                    else -> {
                        Icons.Outlined.BrightnessLow
                    }
                },
                contentDescription = "Score icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
            )
            CustomSpacer(size = 8.dp)
            Text(text = state.reviewScore.toString(), style = primaryBoldDisplayS)
        }
        CustomSpacer(size = 24.dp)
        if (!canContinue) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB600))
            ) {
                Text(
                    text = stringResource(id = R.string.rules_publish_review),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color.White,
                    style = secondaryRegularBodyM
                )
            }
            CustomSpacer(size = 24.dp)
        }
        SunsetButton(
            text = R.string.continue_text,
            enabled = canContinue,
            onClick = {
                onContinueClick()
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 24.dp)
    }

    if (state.showFinishedDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowFinishedDialog(it) },
            dialogTitle = R.string.post_review_finished,
            dialogDescription = R.string.post_review_finished_description,
            dismissButtonText = R.string.continue_text,
            dismissClickedAction = {
                navController.navigate(SunsetRoutes.DiscoverScreen.route) {
                    popUpTo(SunsetRoutes.AddSpotReviewScreen.route) { inclusive = true }
                }
            }
        )
    }

    when (state.uploadProgress) {
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
            if (state.uploadProgress.data != "") {
                LaunchedEffect(key1 = state.uploadProgress.data) {
                    setShowFinishedDialog(true)
                    clearUploadProgress()
                }
            } else if (state.uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            }
        }

        else -> {
            //Not necessary
        }
    }

}
