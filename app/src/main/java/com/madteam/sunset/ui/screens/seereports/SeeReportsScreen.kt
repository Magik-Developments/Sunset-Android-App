package com.madteam.sunset.ui.screens.seereports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.screens.seereports.state.SeeReportsUIEvent
import com.madteam.sunset.ui.screens.seereports.state.SeeReportsUIState
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM

@Composable
fun SeeReportsScreen(
    navController: NavController,
    viewModel: SeeReportsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.reports, onClick = { navController.popBackStack() })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SeeReportsContent(
                    state = state,
                    clickOnAssignReport = { viewModel.onEvent(SeeReportsUIEvent.AssignReport(it)) },
                    clickOnDeleteReport = { viewModel.onEvent(SeeReportsUIEvent.DeleteReport(it)) },
                    clickOnResolveReport = { viewModel.onEvent(SeeReportsUIEvent.DeleteReport(it)) },
                    navigateTo = navController::navigate
                )
            }
        }
    )
}

@Composable
fun SeeReportsContent(
    state: SeeReportsUIState,
    clickOnAssignReport: (String) -> Unit,
    clickOnDeleteReport: (String) -> Unit,
    clickOnResolveReport: (String) -> Unit,
    navigateTo: (String) -> Unit
) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn {
            itemsIndexed(state.reportsList.sortedBy {
                it.date
            }) { _, report ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .wrapContentHeight()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .clickable {
                            if (report.type.contains("spot", ignoreCase = true)) {
                                val spotId = report.docReference.id
                                navigateTo("spot_detail_screen/spotReference=${spotId}")
                            }
                        }
                ) {
                    val (reportId, reportType, reportDescription, reportedByUsername, reportedDate, assignedBy, assignButton, deleteButton, resolveButton, copyToClipboard) = createRefs()
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .constrainAs(reportId) {
                                top.linkTo(parent.top, 8.dp)
                                start.linkTo(parent.start)
                            },
                        text = "#${report.id}",
                        style = primaryBoldHeadlineXS
                    )
                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .constrainAs(copyToClipboard) {
                                top.linkTo(reportId.top)
                                bottom.linkTo(reportId.bottom)
                                start.linkTo(reportId.end)
                            },
                        onClick = {
                            clipboardManager.setText(AnnotatedString(report.id))
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.ContentPaste,
                            contentDescription = "Copy to clipboard",
                            tint = Color(0xFFD9D9D9)
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .constrainAs(reportType) {
                                top.linkTo(reportId.bottom, 8.dp)
                                start.linkTo(parent.start)
                            },
                        text = "${report.type.uppercase()} | ${report.issue}",
                        style = secondarySemiBoldHeadLineM
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .constrainAs(reportDescription) {
                                top.linkTo(reportType.bottom, 8.dp)
                                start.linkTo(parent.start)
                            },
                        text = report.description,
                        style = secondaryRegularBodyL,
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .constrainAs(reportedByUsername) {
                                top.linkTo(reportDescription.bottom, 8.dp)
                                start.linkTo(parent.start)
                            },
                        text = stringResource(id = R.string.reported_by) + " ${report.reporter}",
                        style = secondarySemiBoldBodyM
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .constrainAs(reportedDate) {
                                top.linkTo(reportedByUsername.bottom, 4.dp)
                                start.linkTo(parent.start)
                            },
                        text = report.date,
                        style = secondaryRegularBodyM
                    )
                    if (report.assignedBy.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 16.dp, top = 16.dp)
                                .constrainAs(assignedBy) {
                                    top.linkTo(resolveButton.top)
                                    bottom.linkTo(resolveButton.bottom)
                                    start.linkTo(parent.start)
                                },
                            text = stringResource(id = R.string.assigned_by) + ": ${report.assignedBy}",
                            style = secondarySemiBoldBodyM
                        )
                        IconButton(
                            modifier = Modifier
                                .padding()
                                .constrainAs(resolveButton) {
                                    top.linkTo(reportedDate.bottom)
                                    start.linkTo(assignedBy.end)
                                },
                            onClick = { clickOnResolveReport(report.id) }) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Resolve report",
                                tint = Color.Blue
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 16.dp)
                                .constrainAs(assignedBy) {
                                    top.linkTo(assignButton.top)
                                    bottom.linkTo(assignButton.bottom)
                                },
                            text = stringResource(id = R.string.unnasigned_report).uppercase(),
                            style = secondarySemiBoldBodyM
                        )
                        IconButton(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .constrainAs(assignButton) {
                                    top.linkTo(reportedDate.bottom)
                                    start.linkTo(assignedBy.end, 16.dp)
                                },
                            onClick = { clickOnAssignReport(report.id) }) {
                            Icon(
                                imageVector = Icons.Filled.VerifiedUser,
                                contentDescription = "Assign to me",
                                tint = Color.Green
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .constrainAs(deleteButton) {
                                    top.linkTo(reportedDate.bottom)
                                    start.linkTo(assignButton.end)
                                },
                            onClick = { clickOnDeleteReport(report.id) }) {
                            Icon(
                                imageVector = Icons.Filled.DeleteForever,
                                contentDescription = "Delete report",
                                tint = Color.Red
                            )
                        }
                    }

                }

            }
        }

    }

}