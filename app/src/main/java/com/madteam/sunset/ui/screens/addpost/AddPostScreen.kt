package com.madteam.sunset.ui.screens.addpost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.common.IconButtonDark
import com.madteam.sunset.utils.shimmerBrush

@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = hiltViewModel(),
    spotReference: String,
    navController: NavController
) {

    val selectedImageUris by viewModel.selectedImageUris.collectAsStateWithLifecycle()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> viewModel.updateSelectedImages(uris) })

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_post,
                onQuitClick = { navController.popBackStack() },
                onContinueClick = { /*TODO*/ },
                canContinue = true
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddPostContent(selectedImages = selectedImageUris,
                    onAddImagesClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    )

}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddPostContent(
    selectedImages: List<Uri>,
    onAddImagesClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
        AutoSlidingCarousel(
            itemsCount = selectedImages.size,
            autoSlideDuration = 0,
            itemContent = { index ->
                GlideImage(
                    model = selectedImages[index],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(388.dp)
        )
        LazyRow {
            itemsIndexed(selectedImages) { _, image ->
                GlideImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        CustomSpacer(size = 24.dp)
        IconButtonDark(
            buttonIcon = Icons.Default.Add,
            description = R.string.add,
            onClick = { onAddImagesClick() },
            iconTint = Color.White
        )
    }
}