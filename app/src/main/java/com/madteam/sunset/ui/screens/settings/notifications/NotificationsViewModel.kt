package com.madteam.sunset.ui.screens.settings.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(

) : ViewModel() {

    private val _showNotificationsPermissionDialog: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val showNotificationsPermissionDialog: StateFlow<Boolean> = _showNotificationsPermissionDialog

    fun setShowNotificationsPermissionDialog(state: Boolean) {
        _showNotificationsPermissionDialog.value = state
    }

}