@file:OptIn(ExperimentalMaterialApi::class)

package com.madteam.sunset.welcome.ui.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.lifecycle.ViewModel
import com.madteam.sunset.welcome.ui.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

private const val ANIM_DURATION = 500

@HiltViewModel
class WelcomeViewModel @Inject constructor(
  private val authViewModel: AuthViewModel
) : ViewModel() {

  private val _sheetState: MutableStateFlow<ModalBottomSheetState> =
    MutableStateFlow(ModalBottomSheetState(Hidden, isSkipHalfExpanded = true))
  val sheetState: StateFlow<ModalBottomSheetState> = _sheetState

  val signInState = authViewModel.signInState

  @OptIn(ExperimentalMaterialApi::class)
  suspend fun expandBottomSheet() {
    val animationSpec =
      TweenSpec<Float>(durationMillis = ANIM_DURATION, easing = FastOutSlowInEasing)
    _sheetState.value.animateTo(Expanded, animationSpec)
  }
}
