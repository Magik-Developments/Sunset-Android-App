package com.madteam.sunset.ui.screens.editprofile.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.R
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.GenericTextField
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightChangeImageButton
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.screens.editprofile.state.EditProfileUIEvent
import com.madteam.sunset.ui.screens.editprofile.state.EditProfileUIState
import com.madteam.sunset.ui.screens.editprofile.viewmodel.EditProfileViewModel
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.BackPressHandler
import com.madteam.sunset.utils.Resource

@Composable
fun BottomSheetEditProfileScreen(
    onCloseButton: () -> Unit,
    onProfileUpdated: (UserProfile) -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BackPressHandler {
        onCloseButton()
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onEvent(EditProfileUIEvent.UpdateProfileImage(uri))
            }
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * 0.98).dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BottomSheetEditProfileContent(
            state = state,
            updateName = { viewModel.onEvent(EditProfileUIEvent.UpdateName(it)) },
            updateLocation = { viewModel.onEvent(EditProfileUIEvent.UpdateLocation(it)) },
            saveData = { viewModel.onEvent(EditProfileUIEvent.SaveData) },
            onEditProfileImageClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            clearUploadProgress = { viewModel.onEvent(EditProfileUIEvent.ClearUploadProgress) },
            onCloseButton = onCloseButton,
            onProfileUpdated = onProfileUpdated
        )
    }
}

@Composable
fun BottomSheetEditProfileContent(
    state: EditProfileUIState,
    updateName: (String) -> Unit,
    updateLocation: (String) -> Unit,
    saveData: () -> Unit,
    onEditProfileImageClick: () -> Unit,
    clearUploadProgress: () -> Unit,
    onCloseButton: () -> Unit,
    onProfileUpdated: (UserProfile) -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 16.dp)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CloseIconButton { onCloseButton() }
            CustomSpacer(size = 16.dp)
            Text(
                text = stringResource(id = R.string.edit_profile),
                style = secondarySemiBoldHeadLineS
            )
        }
        CustomSpacer(size = 36.dp)
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            val (profileImage, changeImageButton) = createRefs()
            ProfileImage(
                imageUrl = state.userImage,
                size = 150.dp,
                modifier = Modifier.constrainAs(profileImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            RoundedLightChangeImageButton(onClick = { onEditProfileImageClick() },
                modifier = Modifier.constrainAs(changeImageButton)
                {
                    bottom.linkTo(profileImage.bottom)
                    start.linkTo(profileImage.end, (-32).dp)
                })
        }
        CustomSpacer(size = 36.dp)
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.username),
                style = secondarySemiBoldBodyL,
                color = Color(0xFF333333)
            )
            CustomSpacer(size = 8.dp)
            UsernameTextField(
                usernameValue = state.username,
                onValueChange = {/* to do */ },
                enabled = false
            )
            CustomSpacer(size = 16.dp)
            Text(
                text = stringResource(id = R.string.email_address),
                style = secondarySemiBoldBodyL,
                color = Color(0xFF333333)
            )
            CustomSpacer(size = 8.dp)
            EmailTextField(
                emailValue = state.email,
                onValueChange = {/* to do */ },
                enabled = false
            )
            CustomSpacer(size = 16.dp)
            Text(
                text = stringResource(R.string.name),
                style = secondarySemiBoldBodyL,
                color = Color(0xFF333333)
            )
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = state.name,
                onValueChange = {
                    updateName(it)
                },
                hint = R.string.name
            )
            CustomSpacer(size = 16.dp)
            Text(
                text = stringResource(R.string.location),
                style = secondarySemiBoldBodyL,
                color = Color(0xFF333333)
            )
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = state.location,
                onValueChange = {
                    updateLocation(it)
                },
                hint = R.string.location
            )
            CustomSpacer(size = 16.dp)
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            SmallButtonDark(onClick = {
                saveData()
            }, text = R.string.save, enabled = state.dataHasChanged)
            CustomSpacer(size = 24.dp)
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
            if (state.uploadProgress.data != "") {
                LaunchedEffect(key1 = state.uploadProgress.data) {
                    Toast.makeText(context, R.string.data_updated, Toast.LENGTH_LONG).show()
                    clearUploadProgress()
                    onProfileUpdated(
                        UserProfile(
                            username = state.username,
                            email = state.email,
                            provider = "",
                            creation_date = "",
                            name = state.name,
                            location = state.location,
                            image = state.userImage,
                            admin = false
                        )
                    )
                }
            } else if (state.uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            }
        }

        else -> {
            // Not implemented yet
        }
    }
}