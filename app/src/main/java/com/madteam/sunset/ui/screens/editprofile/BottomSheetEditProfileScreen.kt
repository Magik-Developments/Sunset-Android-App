package com.madteam.sunset.ui.screens.editprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.GenericTextField
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.theme.SunsetTheme
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

@Composable
fun BottomSheetEditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val username by viewModel.username.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val userImage by viewModel.userImage.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * 0.98).dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BottomSheetEditProfileContent(
            usernameValue = username,
            emailValue = email,
            nameValue = name,
            locationValue = location,
            userImage = userImage,
            updateName = viewModel::updateName,
            updateLocation = viewModel::updateLocation,
            saveData = viewModel::updateData
        )
    }
}

@Composable
fun BottomSheetEditProfileContent(
    usernameValue: String,
    emailValue: String,
    nameValue: String,
    locationValue: String,
    userImage: String,
    updateName: (String) -> Unit,
    updateLocation: (String) -> Unit,
    saveData: () -> Unit
) {

    var dataHasChanged by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 16.dp)
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            CloseIconButton { /* to do */ }
            CustomSpacer(size = 16.dp)
            Text(text = "Edit profile", style = secondarySemiBoldHeadLineS)
        }
        CustomSpacer(size = 36.dp)
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(imageUrl = userImage, size = 150.dp)
            CustomSpacer(size = 36.dp)
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Username", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            UsernameTextField(
                usernameValue = usernameValue,
                onValueChange = {/* to do */ },
                enabled = false
            )
            CustomSpacer(size = 16.dp)
            Text(text = "Email address", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            EmailTextField(emailValue = emailValue, onValueChange = {/* to do */ }, enabled = false)
            CustomSpacer(size = 16.dp)
            Text(text = "Name", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = nameValue,
                onValueChange = {
                    updateName(it)
                    dataHasChanged = true
                },
                hint = R.string.name
            )
            CustomSpacer(size = 16.dp)
            Text(text = "Location", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = locationValue,
                onValueChange = {
                    updateLocation(it)
                    dataHasChanged = true
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
                dataHasChanged = false
            }, text = R.string.save, enabled = dataHasChanged)
            CustomSpacer(size = 24.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheetEditProfileContent() {
    SunsetTheme {
        BottomSheetEditProfileContent(
            usernameValue = "usernameValue",
            emailValue = "emailValue",
            nameValue = "nameValue",
            locationValue = "locationValue",
            userImage = "userImage",
            updateName = { _ -> },
            updateLocation = { _ -> },
            saveData = {}
        )
    }
}