package com.madteam.sunset.ui.screens.seereports.state

import com.madteam.sunset.data.model.Report

data class SeeReportsUIState(
    val reportsList: List<Report> = listOf()
)