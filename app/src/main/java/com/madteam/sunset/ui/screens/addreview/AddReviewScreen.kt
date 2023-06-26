package com.madteam.sunset.ui.screens.addreview

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS

private const val MAX_CHAR_LENGTH_REVIEW_TITLE = 50
private const val MAX_CHAR_LENGTH_REVIEW_DESCRIPTION = 2500

@Composable
fun AddReviewScreen(
    spotReference: String,
    viewModel: AddReviewViewModel = hiltViewModel(),
    navController: NavController
) {

    val isReadyToPost = false

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_review,
                onQuitClick = {
                    if (isReadyToPost) {
                        // show dialog before quitting
                    } else {
                        navController.popBackStack()
                    }
                },
                onContinueClick = {
                    // post review
                },
                canContinue = isReadyToPost
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddReviewContent()
            }
        }
    )
}

@Composable
fun AddReviewContent() {

    val context = LocalContext.current
    var temporalTitle by remember {
        mutableStateOf("")
    }
    var temporalDescription by remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        CustomTextField(
            value = temporalTitle,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_REVIEW_TITLE) {
                    // update value changed
                    temporalTitle = it
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
        CustomSpacer(size = 24.dp)
        CustomTextField(
            value = temporalDescription,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_REVIEW_DESCRIPTION) {
                    // update value changed
                    temporalDescription = it
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
        CustomSpacer(size = 24.dp)
        Text(text = stringResource(id = R.string.how_it_was), style = primaryBoldHeadlineL)
        CustomSpacer(size = 4.dp)
        Text(
            text = stringResource(id = R.string.add_attributes_review),
            style = secondarySemiBoldBodyM
        )
        CustomSpacer(size = 16.dp)
    }

}
