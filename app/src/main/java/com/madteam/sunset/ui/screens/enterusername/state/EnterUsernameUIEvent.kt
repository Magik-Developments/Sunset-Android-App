package com.madteam.sunset.ui.screens.enterusername.state

sealed class EnterUsernameUIEvent {
    data class UsernameChanged(val username: String) : EnterUsernameUIEvent()
    data object ContinueClicked : EnterUsernameUIEvent()
    data class SetShowDialog(val showDialog: Boolean) : EnterUsernameUIEvent()
    data object AcceptDialogClicked : EnterUsernameUIEvent()
    data object ClearState : EnterUsernameUIEvent()
}