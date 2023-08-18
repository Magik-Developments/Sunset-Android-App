package com.madteam.sunset.ui.screens.addspot

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.screens.addpost.MAX_IMAGES_SELECTED
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.MapStyles
import com.madteam.sunset.utils.googlemaps.setMapProperties
import com.madteam.sunset.utils.googlemaps.updateCameraLocation
import com.madteam.sunset.utils.shimmerBrush
import kotlinx.coroutines.launch

private const val MAX_CHAR_LENGTH_SPOT_TITLE = 24
private const val MAX_CHAR_LENGTH_SPOT_DESCRIPTION = 580

@Composable
fun AddSpotScreen(
    navController: NavController,
    viewModel: AddSpotViewModel = hiltViewModel(),
    selectedLocation: LatLng = LatLng(0.0, 0.0)
) {

    val imageUris by viewModel.imageUris.collectAsStateWithLifecycle()
    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val spotTitle by viewModel.spotTitle.collectAsStateWithLifecycle()
    val spotDescription by viewModel.spotDescription.collectAsStateWithLifecycle()
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val locationLocality by viewModel.spotLocationLocality.collectAsStateWithLifecycle()
    val locationCountry by viewModel.spotLocationCountry.collectAsStateWithLifecycle()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES_SELECTED),
        onResult = { uris -> viewModel.updateSelectedImages(uris) })

    viewModel.modifySpotLocation(selectedLocation)
    viewModel.obtainCountryAndCityFromLatLng()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_spot,
                onQuitClick = { navController.popBackStack() /* TODO: showExitDialog if it is ready to post */ },
                onContinueClick = { /*TODO*/ },
                canContinue = false
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddSpotContent(
                    images = imageUris,
                    selectedImage = selectedImageUri,
                    onImageSelected = viewModel::addSelectedImage,
                    onAddImagesClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onDeleteImagesClick = viewModel::removeSelectedImageFromList,
                    spotTitle = spotTitle,
                    spotDescription = spotDescription,
                    onSpotTitleChanged = viewModel::modifySpotTitle,
                    onSpotDescriptionChanged = viewModel::modifySpotDescription,
                    navigateTo = navController::navigate,
                    selectedLocation = selectedLocation,
                    mapState = mapState,
                    locationLocality = locationLocality,
                    locationCountry = locationCountry
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddSpotContent(
    images: List<Uri>,
    selectedImage: Uri,
    onImageSelected: (Uri) -> Unit,
    onAddImagesClick: () -> Unit,
    onDeleteImagesClick: () -> Unit,
    spotTitle: String,
    onSpotTitleChanged: (String) -> Unit,
    spotDescription: String,
    onSpotDescriptionChanged: (String) -> Unit,
    navigateTo: (String) -> Unit,
    selectedLocation: LatLng,
    mapState: MapState,
    locationLocality: String,
    locationCountry: String
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isLocationSelected = (selectedLocation.longitude != 0.0 && selectedLocation.latitude != 0.0)
    val cameraPositionState = rememberCameraPositionState()

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        LaunchedEffect(key1 = selectedLocation) {
            if (selectedLocation.latitude != 0.0 && selectedLocation.longitude != 0.0) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }

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
                            .clickable {
                                onImageSelected(image)
                            }
                    )
                    if (image == selectedImage) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color(0xCCFFB600)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { onDeleteImagesClick() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete image",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                IconButton(
                    onClick = { onAddImagesClick() },
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFFFFB600))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add image",
                        tint = Color.White
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
                    userLocation = selectedLocation
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
            Button(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp),
                onClick = {
                    if (isLocationSelected) {
                        navigateTo("select_location_screen/lat=${selectedLocation.latitude}long=${selectedLocation.longitude}")
                    } else {
                        navigateTo(SunsetRoutes.SelectLocationScreen.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB600))
            ) {
                if (!isLocationSelected) {
                    Text(text = stringResource(id = R.string.add_location), color = Color.White)
                } else {
                    Text(
                        text = stringResource(id = R.string.modify_location),
                        color = Color.White
                    )
                }
            }
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