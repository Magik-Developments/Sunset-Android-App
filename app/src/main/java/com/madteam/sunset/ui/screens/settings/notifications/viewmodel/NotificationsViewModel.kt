package com.madteam.sunset.ui.screens.settings.notifications.viewmodel

import androidx.lifecycle.ViewModel
import com.madteam.sunset.ui.screens.settings.notifications.state.NotificationsUIEvent
import com.madteam.sunset.ui.screens.settings.notifications.state.NotificationsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
) : ViewModel() {

    private val _state: MutableStateFlow<NotificationsUIState> =
        MutableStateFlow(NotificationsUIState())
    val state: StateFlow<NotificationsUIState> = _state

    fun onEvent(event: NotificationsUIEvent) {
        when (event) {
            is NotificationsUIEvent.SetShowNotificationsPermissionDialog -> {
                setShowNotificationsPermissionDialog(event.state)
            }
        }
    }

    private fun setShowNotificationsPermissionDialog(state: Boolean) {
        _state.value = _state.value.copy(showNotificationsPermissionDialog = state)
    }

}