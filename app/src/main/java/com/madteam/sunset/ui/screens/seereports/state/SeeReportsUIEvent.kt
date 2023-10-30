package com.madteam.sunset.ui.screens.seereports.state

sealed class SeeReportsUIEvent {
    data class AssignReport(val reportId: String) : SeeReportsUIEvent()
    data class DeleteReport(val reportId: String) : SeeReportsUIEvent()
}