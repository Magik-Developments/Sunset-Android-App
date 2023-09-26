package com.madteam.sunset.ui.screens.editspot

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.common.LargeDangerButton
import com.madteam.sunset.ui.common.ScoreSlider
import com.madteam.sunset.ui.screens.addreview.FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.NON_FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.SUNSET_ATTRIBUTES
import com.madteam.sunset.ui.theme.primaryBoldDisplayS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.getResourceId
import com.madteam.sunset.utils.googlemaps.MapState
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

    viewModel.setSpotReference("spots/$spotReference")

    val spotInfo by viewModel.spotInfo.collectAsStateWithLifecycle()
    val imageUris by viewModel.imageUris.collectAsStateWithLifecycle()
    val spotTitle by viewModel.spotTitle.collectAsStateWithLifecycle()
    val spotDescription by viewModel.spotDescription.collectAsStateWithLifecycle()
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val locationLocality: String? by viewModel.spotLocationLocality.collectAsStateWithLifecycle()
    val locationCountry: String? by viewModel.spotLocationCountry.collectAsStateWithLifecycle()
    val attributesList by viewModel.attributesList.collectAsStateWithLifecycle()
    val selectedAttributes by viewModel.selectedAttributes.collectAsStateWithLifecycle()
    val spotScore by viewModel.spotScore.collectAsStateWithLifecycle()
    val uploadProgress by viewModel.uploadProgress.collectAsStateWithLifecycle()
    val showExitDialog by viewModel.showExitDialog.collectAsStateWithLifecycle()
    val errorToastText by viewModel.errorToastText.collectAsStateWithLifecycle()
    val spotLocation by viewModel.spotLocation.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()

    viewModel.modifySpotLocation(selectedLocation)
    viewModel.obtainCountryAndCityFromLatLng()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.edit_spot,
                onQuitClick = { navController.popBackStack() /* TODO: showExitDialog if it is ready to post */ },
                onContinueClick = { viewModel.updateSpotIntent() },
                canContinue = ((spotScore != spotInfo.score.toInt() &&
                        spotScore != 0 || selectedAttributes != spotInfo.attributes &&
                        selectedAttributes.isNotEmpty() || spotTitle != spotInfo.name &&
                        spotTitle.isNotEmpty() || spotDescription != spotInfo.description &&
                        spotDescription.isNotEmpty())
                        )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddSpotContent(
                    images = imageUris,
                    spotTitle = spotTitle,
                    spotDescription = spotDescription,
                    onSpotTitleChanged = viewModel::modifySpotTitle,
                    onSpotDescriptionChanged = viewModel::modifySpotDescription,
                    navigateTo = navController::navigate,
                    selectedLocation = selectedLocation,
                    mapState = mapState,
                    locationLocality = locationLocality ?: "",
                    locationCountry = locationCountry ?: "",
                    attributesList = attributesList,
                    selectedAttributes = selectedAttributes,
                    onAttributeClicked = viewModel::modifySelectedAttributes,
                    spotScore = spotScore,
                    onReviewScoreChanged = viewModel::modifyReviewScore,
                    uploadProgress = uploadProgress,
                    clearUploadProgress = viewModel::clearUploadProgressState,
                    clearErrorToast = viewModel::clearErrorToastText,
                    errorToast = errorToastText,
                    exitAddSpot = navController::popBackStack,
                    setShowExitDialog = viewModel::setShowExitDialog,
                    showExitDialog = showExitDialog,
                    spotLocation = spotLocation,
                    showDeleteDialog = showDeleteDialog,
                    setShowDeleteDialog = viewModel::setShowDeleteDialog,
                    deleteSpot = viewModel::deleteSpotIntent
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddSpotContent(
    images: List<String>,
    spotTitle: String,
    onSpotTitleChanged: (String) -> Unit,
    spotDescription: String,
    onSpotDescriptionChanged: (String) -> Unit,
    navigateTo: (String) -> Unit,
    selectedLocation: LatLng,
    mapState: MapState,
    locationLocality: String,
    locationCountry: String,
    attributesList: List<SpotAttribute>,
    selectedAttributes: List<SpotAttribute>,
    onAttributeClicked: (SpotAttribute) -> Unit,
    spotScore: Int,
    onReviewScoreChanged: (Float) -> Unit,
    uploadProgress: Resource<String>,
    clearUploadProgress: () -> Unit,
    errorToast: String,
    clearErrorToast: () -> Unit,
    showExitDialog: Boolean,
    exitAddSpot: () -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    spotLocation: LatLng,
    showDeleteDialog: Boolean,
    setShowDeleteDialog: (Boolean) -> Unit,
    deleteSpot: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()

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
                exitAddSpot()
            }
        )
    }

    if (showDeleteDialog) {
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
                itemsCount = images.size,
                autoSlideDuration = 0,
                itemContent = { index ->
                    GlideImage(
                        model = images[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
            )
            if (images.isEmpty()) {
                Text(
                    text = "Add the best photos of this Spot",
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
            itemsIndexed(images) { _, image ->
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
            value = spotTitle,
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
            value = spotDescription,
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
                properties = setMapProperties(mapState = mapState, MapStyles.NAKED),
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
                    userLocation = spotLocation
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
            /* Button(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp),
                onClick = {
                    navigateTo("select_location_screen/lat=${spotLocation.latitude}long=${spotLocation.longitude}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB600))
            ) {
                Text(
                    text = stringResource(id = R.string.modify_location),
                    color = Color.White
                )
            } */
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(text = locationCountry, style = primaryBoldHeadlineM, color = Color.White)
                Text(text = locationLocality, style = primaryMediumHeadlineS, color = Color.White)
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
            value = spotScore.toFloat(),
            onValueChange = { onReviewScoreChanged(it) },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (spotScore < 3) {
                    Icons.Outlined.BrightnessLow
                } else if (spotScore < 5) {
                    Icons.Outlined.Brightness4
                } else if (spotScore < 7) {
                    Icons.Outlined.Brightness5
                } else if (spotScore < 9) {
                    Icons.Outlined.Brightness6
                } else if (spotScore > 9) {
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
            Text(text = spotScore.toString(), style = primaryBoldDisplayS)
        }
        CustomSpacer(size = 56.dp)
        LargeDangerButton(
            onClick = { setShowDeleteDialog(true) },
            text = R.string.delete_spot,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 24.dp)
    }

    if (images.isEmpty()) {
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
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    CircularLoadingDialog()
                }
            }
        }

        is Resource.Success -> {
            if (uploadProgress.data != "") {
                LaunchedEffect(key1 = uploadProgress.data) {
                    navigateTo("spot_detail_screen/spotReference=${uploadProgress.data}")
                    clearUploadProgress()
                }
            } else if (uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            } else if (uploadProgress.data.contains("deleted")) {
                Toast.makeText(context, "Spot deleted successfully.", Toast.LENGTH_SHORT).show()
                navigateTo(SunsetRoutes.DiscoverScreen.route)
                clearUploadProgress()
            }
        }

        else -> {}
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