package com.madteam.sunset.ui.screens.settings.notifications.state

sealed class NotificationsUIEvent {
    data class SetShowNotificationsPermissionDialog(val state: Boolean) : NotificationsUIEvent()
}