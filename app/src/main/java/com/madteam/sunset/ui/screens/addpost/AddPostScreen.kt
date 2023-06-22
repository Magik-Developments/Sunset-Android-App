package com.madteam.sunset.ui.screens.addpost

import android.net.Uri
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.AddDescriptionTextField
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.utils.shimmerBrush

private const val MAX_IMAGES_SELECTED = 8

@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = hiltViewModel(),
    spotReference: String,
    navController: NavController
) {

    val imageUris by viewModel.imageUris.collectAsStateWithLifecycle()
    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val descriptionText by viewModel.descriptionText.collectAsStateWithLifecycle()
    val showExitDialog by viewModel.showExitDialog.collectAsStateWithLifecycle()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES_SELECTED),
        onResult = { uris -> viewModel.updateSelectedImages(uris) })

    val isReadyToPost = imageUris.isNotEmpty()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_post,
                onQuitClick = {
                    if (isReadyToPost) {
                        viewModel.setShowExitDialog(true)
                    } else {
                        navController.popBackStack()
                    }
                },
                onContinueClick = { /*TODO*/ },
                canContinue = isReadyToPost
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddPostContent(
                    images = imageUris,
                    selectedImage = selectedImageUri,
                    descriptionText = descriptionText,
                    onImageSelected = viewModel::addSelectedImage,
                    onAddImagesClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onDeleteImagesClick = viewModel::removeSelectedImageFromList,
                    onUpdateDescriptionText = viewModel::updateDescriptionText,
                    showExitDialog = showExitDialog,
                    setShowExitDialog = viewModel::setShowExitDialog,
                    exitAddPost = navController::popBackStack
                )
            }
        }
    )

}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddPostContent(
    images: List<Uri>,
    selectedImage: Uri,
    descriptionText: String,
    onImageSelected: (Uri) -> Unit,
    onAddImagesClick: () -> Unit,
    showExitDialog: Boolean,
    onDeleteImagesClick: () -> Unit,
    onUpdateDescriptionText: (String) -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    exitAddPost: () -> Unit
) {

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
                exitAddPost()
            }
        )
    }

    Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
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
        CustomSpacer(size = 8.dp)
        AddDescriptionTextField(
            value = descriptionText,
            onValueChange = { onUpdateDescriptionText(it) },
            hint = R.string.add_post_description
        )
    }
}