package com.madteam.sunset.ui.screens.seereports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoBackTopAppBar

@Composable
fun SeeReportsScreen(
    navController: NavController,
    viewModel: SeeReportsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.reports, onClick = { navController.popBackStack() })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SeeReportsContent()
            }
        }
    )
}

@Composable
fun SeeReportsContent(

) {

}