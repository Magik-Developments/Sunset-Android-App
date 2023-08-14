package com.madteam.sunset.ui.screens.addspot

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.screens.addpost.MAX_IMAGES_SELECTED
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.shimmerBrush

private const val MAX_CHAR_LENGTH_SPOT_TITLE = 24
private const val MAX_CHAR_LENGTH_SPOT_DESCRIPTION = 580

@Composable
fun AddSpotScreen(
    navController: NavController,
    viewModel: AddSpotViewModel = hiltViewModel()
) {

    val imageUris by viewModel.imageUris.collectAsStateWithLifecycle()
    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val spotTitle by viewModel.spotTitle.collectAsStateWithLifecycle()
    val spotDescription by viewModel.spotDescription.collectAsStateWithLifecycle()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES_SELECTED),
        onResult = { uris -> viewModel.updateSelectedImages(uris) })

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
                    navigateTo = navController::navigate
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
    navigateTo: (String) -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                .background(Color(0xFFD9D9D9))
                .clickable {
                    navigateTo(SunsetRoutes.SelectLocationScreen.route)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.add_location),
                    style = secondarySemiBoldHeadLineS,
                    color = Color(0xFF666666)
                )
                Text(text = "🗺️📍", fontSize = TextUnit(24f, TextUnitType.Sp))
            }
        }

    }
}