package com.madteam.sunset.ui.screens.editspot.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AttributesBigListSelectable
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.common.LargeDangerButton
import com.madteam.sunset.ui.common.ScoreSlider
import com.madteam.sunset.ui.screens.addreview.ui.FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.ui.NON_FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.ui.SUNSET_ATTRIBUTES
import com.madteam.sunset.ui.screens.editspot.state.EditSpotUIEvent
import com.madteam.sunset.ui.screens.editspot.state.EditSpotUIState
import com.madteam.sunset.ui.screens.editspot.viewmodel.EditSpotViewModel
import com.madteam.sunset.ui.theme.primaryBoldDisplayS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.googlemaps.MapStyles
import com.madteam.sunset.utils.googlemaps.setMapProperties
import com.madteam.sunset.utils.googlemaps.updateCameraLocation
import com.madteam.sunset.utils.shimmerBrush

private const val MAX_CHAR_LENGTH_SPOT_TITLE = 24
private const val MAX_CHAR_LENGTH_SPOT_DESCRIPTION = 580

@Composable
fun EditSpotScreen(
    navController: NavController,
    viewModel: EditSpotViewModel = hiltViewModel(),
    selectedLocation: LatLng = LatLng(0.0, 0.0),
    spotReference: String
) {

    viewModel.onEvent(EditSpotUIEvent.SetSpotReference("spots/$spotReference"))
    viewModel.onEvent(EditSpotUIEvent.UpdateLocation(selectedLocation))
    viewModel.onEvent(EditSpotUIEvent.GetCountryAndLocality)

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.edit_spot,
                onQuitClick = { navController.popBackStack() },
                onContinueClick = { viewModel.onEvent(EditSpotUIEvent.UpdateSpot) },
                canContinue = (state.spotScore != state.spotInfo.score.toInt() &&
                        state.spotScore != 0 || state.selectedAttributes != state.spotInfo.attributes &&
                        state.selectedAttributes.isNotEmpty() || state.spotTitle != state.spotInfo.name &&
                        state.spotTitle.isNotEmpty() || state.spotDescription != state.spotInfo.description &&
                        state.spotDescription.isNotEmpty())
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddSpotContent(
                    state = state,
                    onSpotTitleChanged = { viewModel.onEvent(EditSpotUIEvent.UpdateTitle(it)) },
                    onSpotDescriptionChanged =
                    { viewModel.onEvent(EditSpotUIEvent.UpdateDescription(it)) },
                    navigateTo = navController::navigate,
                    onAttributeClicked =
                    { viewModel.onEvent(EditSpotUIEvent.UpdateSelectedAttributes(it)) },
                    onReviewScoreChanged = { viewModel.onEvent(EditSpotUIEvent.UpdateScore(it)) },
                    clearUploadProgress = { viewModel.onEvent(EditSpotUIEvent.ClearUploadProgress) },
                    clearErrorToast = { viewModel.onEvent(EditSpotUIEvent.ClearErrorToastText) },
                    exitAddSpot = navController::popBackStack,
                    setShowExitDialog = { viewModel.onEvent(EditSpotUIEvent.ShowExitDialog(it)) },
                    setShowDeleteDialog = { viewModel.onEvent(EditSpotUIEvent.ShowDeleteDialog(it)) },
                    deleteSpot = { viewModel.onEvent(EditSpotUIEvent.DeleteSpot) }
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddSpotContent(
    state: EditSpotUIState,
    onSpotTitleChanged: (String) -> Unit,
    onSpotDescriptionChanged: (String) -> Unit,
    navigateTo: (String) -> Unit,
    onAttributeClicked: (SpotAttribute) -> Unit,
    onReviewScoreChanged: (Float) -> Unit,
    clearUploadProgress: () -> Unit,
    clearErrorToast: () -> Unit,
    exitAddSpot: () -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    setShowDeleteDialog: (Boolean) -> Unit,
    deleteSpot: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val cameraPositionState = rememberCameraPositionState()

    if (state.errorToastText != -1) {
        Toast.makeText(context, state.errorToastText, Toast.LENGTH_SHORT).show()
        clearErrorToast()
    }

    if (state.showDeleteDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowExitDialog(it) },
            dialogTitle = R.string.are_you_sure,
            dialogDescription = R.string.exit_post_dialog,
            positiveButtonText = R.string.discard_changes,
            dismissButtonText = R.string.cancel,
            dismissClickedAction = { setShowExitDialog(false) },
            positiveClickedAction = {
                setShowExitDialog(false)
                exitAddSpot()
            }
        )
    }

    if (state.showDeleteDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowDeleteDialog(it) },
            dialogTitle = R.string.are_you_sure,
            dialogDescription = R.string.delete_spot_description,
            positiveButtonText = R.string.delete_spot,
            dismissButtonText = R.string.cancel,
            dismissClickedAction = { setShowDeleteDialog(false) },
            positiveClickedAction = {
                setShowDeleteDialog(false)
                deleteSpot()
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        //Add featured images section

        Box {
            AutoSlidingCarousel(
                itemsCount = state.imageUris.size,
                autoSlideDuration = 0,
                itemContent = { index ->
                    GlideImage(
                        model = state.imageUris[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
            )
            if (state.imageUris.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.add_images_spot),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    style = primaryBoldHeadlineL,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
        LazyRow {
            itemsIndexed(state.imageUris) { _, image ->
                Box(Modifier.size(150.dp)) {
                    GlideImage(
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                    )
                }
            }
        }

        //Add title and description section

        CustomSpacer(size = 16.dp)
        CustomTextField(
            value = state.spotTitle,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_SPOT_TITLE) {
                    onSpotTitleChanged(it)
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
            value = state.spotDescription,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_SPOT_DESCRIPTION) {
                    onSpotDescriptionChanged(it)
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

        //Add location section

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.Center
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                properties = setMapProperties(mapState = state.mapState, MapStyles.NAKED),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    zoomGesturesEnabled = false,
                    scrollGesturesEnabled = false,
                    indoorLevelPickerEnabled = false,
                    mapToolbarEnabled = false,
                    myLocationButtonEnabled = false,
                    rotationGesturesEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                    tiltGesturesEnabled = false
                )
            ) {
                SetupMapView(
                    cameraPositionState = cameraPositionState,
                    userLocation = state.spotLocation
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = state.spotLocationCountry,
                    style = primaryBoldHeadlineM,
                    color = Color.White
                )
                Text(
                    text = state.spotLocationLocality,
                    style = primaryMediumHeadlineS,
                    color = Color.White
                )
            }
        }

        // Add attributes section
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.add_attributes_review),
            style = secondarySemiBoldHeadLineS,
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

        //Add score section

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
            value = state.spotScore.toFloat(),
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
                    state.spotScore < 3 -> {
                        Icons.Outlined.BrightnessLow
                    }

                    state.spotScore < 5 -> {
                        Icons.Outlined.Brightness4
                    }

                    state.spotScore < 7 -> {
                        Icons.Outlined.Brightness5
                    }

                    state.spotScore < 9 -> {
                        Icons.Outlined.Brightness6
                    }

                    state.spotScore > 9 -> {
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
            Text(text = state.spotScore.toString(), style = primaryBoldDisplayS)
        }
        CustomSpacer(size = 56.dp)
        LargeDangerButton(
            onClick = { setShowDeleteDialog(true) },
            text = R.string.delete_spot,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 24.dp)
    }

    if (state.imageUris.isEmpty()) {
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
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    CircularLoadingDialog()
                }
            }
        }

        is Resource.Success -> {
            when {
                state.uploadProgress.data != "" && state.uploadProgress.data!!.contains("Error") -> {
                    LaunchedEffect(key1 = state.uploadProgress.data) {
                        Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT)
                            .show()
                        clearUploadProgress()
                    }
                }

                state.uploadProgress.data != "" && state.uploadProgress.data.contains("deleted") -> {
                    LaunchedEffect(key1 = state.uploadProgress.data) {
                        Toast.makeText(context, "Spot deleted successfully.", Toast.LENGTH_SHORT)
                            .show()
                        navigateTo(SunsetRoutes.DiscoverScreen.route)
                        clearUploadProgress()
                    }
                }

                state.uploadProgress.data != "" -> {
                    LaunchedEffect(key1 = state.uploadProgress.data) {
                        navigateTo("spot_detail_screen/spotReference=${state.uploadProgress.data}")
                        clearUploadProgress()
                    }
                }
            }
        }

        else -> {
            //Not implemented yet
        }
    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun SetupMapView(
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
) {
    val scope = rememberCoroutineScope()
    MapEffect(key1 = userLocation) { map ->
        if (userLocation.latitude != 0.0 && userLocation.longitude != 0.0) {
            map.updateCameraLocation(scope, cameraPositionState, userLocation, 5f)
        }
    }
}