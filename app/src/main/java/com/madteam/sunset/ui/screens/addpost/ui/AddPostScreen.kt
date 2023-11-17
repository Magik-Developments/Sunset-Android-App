package com.madteam.sunset.ui.screens.addpost.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIState
import com.madteam.sunset.ui.screens.addpost.viewmodel.AddPostViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.shimmerBrush

const val MAX_IMAGES_SELECTED = 8
private const val MAX_CHAR_LENGTH_DESCRIPTION = 2500

@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = hiltViewModel(),
    spotReference: String,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES_SELECTED),
        onResult = { uris -> viewModel.onEvent(AddPostUIEvent.UpdateSelectedImages(uris)) })

    BackHandler {
        if (state.imageUris.isNotEmpty()) {
            viewModel.onEvent(AddPostUIEvent.ShowExitDialog(true))
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            GoBackTopAppBar(
                title = R.string.add_post,
                onClick = {
                    if (state.imageUris.isNotEmpty()) {
                        viewModel.onEvent(AddPostUIEvent.ShowExitDialog(true))
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddPostContent(
                    state = state,
                    navController = navController,
                    onAddImagesClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    exitAddPost = navController::popBackStack,
                    onImageSelected = { viewModel.onEvent(AddPostUIEvent.AddSelectedImage(it)) },
                    onDeleteImagesClick = { viewModel.onEvent(AddPostUIEvent.RemoveSelectedImage) },
                    onUpdateDescriptionText = {
                        viewModel.onEvent(AddPostUIEvent.UpdateDescriptionText(it))
                    },
                    setShowExitDialog = { viewModel.onEvent(AddPostUIEvent.ShowExitDialog(it)) },
                    clearErrorToast = { viewModel.onEvent(AddPostUIEvent.ClearErrorToastText) },
                    clearUploadProgress = { viewModel.onEvent(AddPostUIEvent.ClearUploadProgress) },
                    onContinueClick = { viewModel.onEvent(AddPostUIEvent.CreateNewPost(spotReference)) }
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddPostContent(
    state: AddPostUIState,
    onImageSelected: (Uri) -> Unit,
    onAddImagesClick: () -> Unit,
    onDeleteImagesClick: () -> Unit,
    onUpdateDescriptionText: (String) -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    exitAddPost: () -> Unit,
    clearErrorToast: () -> Unit,
    navController: NavController,
    clearUploadProgress: () -> Unit,
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
                exitAddPost()
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
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
                            .clickable {
                                onImageSelected(image)
                            }
                    )
                    if (image == state.selectedImageUri) {
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
        CustomSpacer(size = 8.dp)
        CustomTextField(
            value = state.descriptionText,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_DESCRIPTION) {
                    onUpdateDescriptionText(it)
                } else {
                    Toast.makeText(context, R.string.max_characters_reached, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            hint = R.string.add_post_description,
            textStyle = secondaryRegularBodyL,
            textColor = Color(0xFF999999)
        )
        CustomSpacer(size = 24.dp)
        if (state.imageUris.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB600))
            ) {
                Text(
                    text = stringResource(id = R.string.rules_publish_post),
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
            enabled = state.imageUris.isNotEmpty(),
            onClick = {
                onContinueClick()
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 24.dp)
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
                    navController.navigate(SunsetRoutes.DiscoverScreen.route) {
                        popUpTo(SunsetRoutes.AddPostScreen.route) { inclusive = true }
                    }
                    clearUploadProgress()
                }
            } else if (state.uploadProgress.data.contains("Error")) {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.generic_error),
                    Toast.LENGTH_SHORT
                ).show()
                clearUploadProgress()
            }
        }

        else -> {
            // Not necessary to implement
        }
    }
}